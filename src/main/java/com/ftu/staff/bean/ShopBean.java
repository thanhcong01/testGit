package com.ftu.staff.bean;

import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import com.ftu.admin.consumer.LogsConsumer;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bean.SessionBean;
import com.ftu.java.bo.LazyShopModel;
import com.ftu.language.LanguageBean;
import com.ftu.utils.StringUtils;
import org.docx4j.model.datastorage.XPathEnhancerParser.main_return;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.TreeNode;

import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bussiness.ShopService;
import com.ftu.staff.dao.ShopDAO;
import org.primefaces.model.Visibility;

@ManagedBean
@ViewScoped
public class ShopBean implements Serializable,Validator {
    private ShopService service = new ShopService();
    private List<Shop> listShops;
    private List<Shop> listShopsAll;
    private TreeNode root;
    private TreeNode selectedNode;
    private HashMap<Long, TreeNode> mapTreeNode;
    private HashMap<Long, Boolean> mapNodeCollapse;
    private String strKeyFilter;
    private String strSearchTree;

    //pham tan add
    private Shop shopParentSelected;
    private Shop shopSelected;
    private Shop shopSelectedOld;
    private LazyDataModel<Shop> lazyShop;
    public String TYPE_EDIT = "edit";
    public String TYPE_ADD = "add";
    public String TYPE_SEARCH = "search";
    private String type;
    @ManagedProperty("#{languageBean}")
    private LanguageBean languageBean;
    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;
    private String resourceLog = InventoryConstanst.RESOURCE_LOG.LOG_SHOP;

    public ShopBean() {
        //init();
    }

    @PostConstruct
    public void init() {
        mapNodeCollapse = new HashMap<Long, Boolean>();
        getAllShop();
        buildTreeShop();
        shopParentSelected = sessionBean.getShop();
        Shop search = new Shop(getSessionBean().getShop().getShopId(),null,null,null,null,null);
        lazyShop = new LazyShopModel(search, false, true);
        setSelectedTreeNode(shopParentSelected.getShopId());
        shopSelected = sessionBean.getShop();
    }

    public void getAllShop() {
        try {
            mapTreeNode = new HashMap<Long, TreeNode>();
            listShopsAll = listShops = service.findAllShopTree(getSessionBean().getShop().getShopId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initTreeShop() {
        mapNodeCollapse = new HashMap<Long, Boolean>();
        this.root = null;
        getAllShop();
        buildTreeShop();
        shopParentSelected = sessionBean.getShop();
        Shop search = new Shop(getSessionBean().getShop().getShopId(),null,null,null,null,null);
        lazyShop = new LazyShopModel(search, false, true);
        setSelectedTreeNode(shopParentSelected.getShopId());
    }
    public void actionTreeShop() {
        mapNodeCollapse = new HashMap<Long, Boolean>();
        this.root = null;
        getAllShop();
        buildTreeShop();
        Shop search = new Shop(shopParentSelected.getShopId(),null,null,null,null,null);
        lazyShop = new LazyShopModel(search, false, true);
        setSelectedTreeNode(shopParentSelected.getShopId());
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("frm:tree");
    }

    public void resetSelectedNode() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        ViewHandler viewHandler = application.getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
        context.setViewRoot(viewRoot);
        context.renderResponse(); //Optional
    }
    private void buildTreeNode(Shop parent, List<Shop> list, TreeNode parentNode) {
        for (Shop s : list) {
            if (s.getShopParentId().equals(parent.getShopId())) {
                TreeNode node = new DefaultTreeNode(s, parentNode);
                mapTreeNode.put(s.getShopId(), node);
                if (mapNodeCollapse.containsKey(s.getShopId())) {
                    node.setExpanded(false);
                } else {
                    node.setExpanded(true);
                }
                List<Shop> listChilds = s.getChildShops();
                if (listChilds.size() > 0) {
                    buildTreeNode(s, listChilds, node);
                }
            }
        }
    }

//    public List<String> completeTree(String gs) {
////        goods = gs;
//        List<String> rs = new ArrayList<>();
//
//        if (listShopsAll != null && !listShopsAll.isEmpty()) {
//            for (Shop g : listShopsAll) {
//                String str = g.getShopCode() + " - " +g.getShopName();
//                if (gs == null || gs.isEmpty() || str.toLowerCase().contains(gs.toLowerCase())) {
//                    rs.add(str);
//                }
//            }
//        }
//        return rs;
//    }
//    public void treesSelect() {
//        if (listShopsAll != null && !listShopsAll.isEmpty()) {
//            for (Shop g : listShopsAll) {
//                String str = g.getShopCode() + " - " +g.getShopName();
//                if (str.toUpperCase().equals(strSearchTree.toUpperCase())) {
//                    mapTreeNode = new HashMap<Long, TreeNode>();
//                    listShops = service.findAllShopTree(g.getShopId());
//                    buildTreeShop();
//                    shopParentSelected = getAllShopParrent().get(0);
//                    break;
//                }
//            }
//        }
//    }

    public void treesSelect() {
//        mapNodeCollapse = new HashMap<Long, Boolean>();
        searchDataForTree();
//        getAllShop();
//        if (listShopsAll != null && !listShopsAll.isEmpty()) {
//            List<Shop> lstResult = new ArrayList<>();
//            if(strSearchTree.trim().isEmpty()){
//                mapNodeCollapse = new HashMap<Long, Boolean>();
//                getAllShop();
//                buildTreeShop();
//                shopParentSelected = getAllShopParrent().get(0);
//            }else{
//                for (Shop g : listShopsAll) {
//                    String str = g.getShopCode() + " - " +g.getShopName();
//                    if (str.toUpperCase().contains(strSearchTree.trim().toUpperCase())) {
//                        lstResult.add(g);
//                    }
//                }
//            }
//
//            if(!lstResult.isEmpty() && lstResult.size()>0){
//                listShops.clear();
//                listShops.addAll(lstResult) ;
//                mapTreeNode = new HashMap<Long, TreeNode>();
//                buildTreeShop();
//                shopParentSelected = listShops.get(0);
//            }else if(!strSearchTree.trim().isEmpty()) {
//                listShops.clear();
//                mapTreeNode = new HashMap<Long, TreeNode>();
//                buildTreeShop();
//            }
//        }

    }

    public List<Shop> getAllShopParrent() {
        return service.getAllShopParrent();
    }

    private void clearTreeDatas() {
        mapTreeNode.clear();
        selectedNode = null;
    }

    public void buildTreeShop() {
        clearTreeDatas();
        this.root = new DefaultTreeNode("", null);
        boolean root = true;
        for (Shop s : listShops) {
//            if ((s.getShopParentId() == null || (s.getChildShops() !=null && s.getChildShops().size() >0)) && root) {
            if (((s.getChildShops() !=null)) && root) {
                TreeNode node = new DefaultTreeNode(s, this.root);
//                if(selectedNode == null){
//                    selectedNode = node;
//                }
                root = false;
                mapTreeNode.put(s.getShopId(), node);
                if (mapNodeCollapse.containsKey(s.getShopId())) {
                    node.setExpanded(false);
                } else {
                    node.setExpanded(true);
                }
                List<Shop> listChilds = s.getChildShops();
                if (listChilds != null) {
                    if (listChilds.size() > 0) {
                        buildTreeNode(s, listChilds, node);
                    }
                }
            }
        }
    }

    private boolean filterObject(Shop s) {
        if (!StringUtil.stringIsNullOrEmty(strKeyFilter)) {
            String str = s.getShopCode() + " - " +s.getShopName();
//            if (s.getAddress().toUpperCase().contains(strKeyFilter.trim().toUpperCase())
//                    || str.toUpperCase().contains(strKeyFilter.trim().toUpperCase())) {
//                return true;
//            }
            if (str.toUpperCase().contains(strKeyFilter.trim().toUpperCase())) {
                return true;
            }
            return false;
        }
        return true;
    }

    private Shop getShopRootForShop(LinkedHashMap<Long, Shop> mapShop, Shop s) {
        Shop parent = s.getParentShop();
        if (parent != null) {
            mapShop.put(parent.getShopId(), parent);
            getShopRootForShop(mapShop, parent);
        }
        return parent;
    }

    private LinkedHashMap<Long, Shop> getChildsForShop(LinkedHashMap<Long, Shop> mapShop, List<Shop> childs,
                                                       Shop shop) {
        childs = shop.getChildShops();
        if (childs != null) {
            mapShop.put(shop.getShopId(), shop);
            for (Shop s : childs) {
                if(filterObject(s)){
                    getChildsForShop(mapShop, childs, s);
                }
            }
        }
        return mapShop;
    }

    public void searchDataForTree() {
        mapNodeCollapse.clear();
        reInitDataForTree();
    }

    private void reInitDataForTree() {
        getAllShop();
        if (StringUtil.stringIsNullOrEmty(strKeyFilter) || strKeyFilter.trim().length() <= 0) {
            buildTreeShop();
            return;
        }

        LinkedHashMap<Long, Shop> mapShop = new LinkedHashMap<Long, Shop>();
        List<Shop> list = new ArrayList<Shop>();
        for (Shop s : listShops) {
            if (filterObject(s)) {
                list.add(s);
                mapShop.put(s.getShopId(), s);
                getChildsForShop(mapShop, s.getChildShops(), s);
            }
        }

        for (Shop s : list) {
            getShopRootForShop(mapShop, s);
        }

        list.clear();
        for (Shop s : listShops) {
            if (mapShop.containsKey(s.getShopId())) {
                s.getChildShops().clear();
                for (Shop s2 : mapShop.values()) {
                    if (s2.getShopParentId() != null && s2.getShopParentId().equals(s.getShopId())) {
                        s.getChildShops().add(s2);
                    }
                }
                list.add(s);
            }
        }

        listShops = list;
        buildTreeShop();
    }

    public void onNodeExpand(NodeExpandEvent event) {
        Shop s = (Shop) event.getTreeNode().getData();
        mapNodeCollapse.remove(s.getShopId());
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        Shop s = (Shop) event.getTreeNode().getData();
        mapNodeCollapse.put(s.getShopId(), false);
    }

    public void setSelectedTreeNode(Shop s) {
        for (Long key : mapTreeNode.keySet()) {
            TreeNode node = mapTreeNode.get(key);
            if (node.isSelected()) {
                node.setSelected(false);
            }
            if (key == s.getShopId()) {
                node.setSelected(true);
            }
        }
    }

    public void setSelectedTreeNode(Long shopId) {
        if (shopId == 0) {

        } else {
            for (Long key : mapTreeNode.keySet()) {
                TreeNode node = mapTreeNode.get(key);
                if (node.isSelected()) {
                    node.setSelected(false);
                }
                if (shopId!=null && key!=null && key.equals(shopId)) {
                    node.setSelected(true);
                    setSelectedNode(node);
                }
            }
        }
    }

    public void setExpandedTreeNode(TreeNode treeNode) {
        treeNode.setExpanded(true);
        TreeNode treeNodeParent = treeNode.getParent();
        if (treeNodeParent != null) {
            treeNodeParent.setExpanded(true);
            setExpandedTreeNode(treeNodeParent);
        }
    }

    public void setSelectedTreeNodeForFilter() {
    }

    public void onNodeSelect(NodeSelectEvent event) {
        shopParentSelected = new Shop();
        Shop s = (Shop) selectedNode.getData();
        if (!selectedNode.getParent().isExpanded()) {
            selectedNode.getParent().setExpanded(true);
        }
        if (!selectedNode.isExpanded()) {
            selectedNode.setExpanded(true);
        }
        shopParentSelected = s;
        Shop search = new Shop(s.getShopId(),null,null,null,null,null);

        // liStaffs = staffService.getAllWithParams(st, false);
        lazyShop = new LazyShopModel(search, false, true);
    }

//    public void onInitNodeSelect(Shop shop) {
//        shopParentSelected = new Shop();
//        if (!selectedNode.getParent().isExpanded()) {
//            selectedNode.getParent().setExpanded(true);
//        }
//        if (!selectedNode.isExpanded()) {
//            selectedNode.setExpanded(true);
//        }
//        shopParentSelected = shop;
//        Shop search = new Shop(shop.getShopId(),null,null,null,null,null);
//
//        // liStaffs = staffService.getAllWithParams(st, false);
//        lazyShop = new LazyShopModel(search, false, true);
//    }

    public void save() {
        FacesMessage message = null;
        try {
            if (type.equals(TYPE_ADD)) {
                shopSelected.setShopStatus(1L);
                shopSelected.setShopParentId(shopParentSelected.getShopId());
            }
            String codePath = shopParentSelected.getCodePath() + "/" + shopSelected.getShopCode();
            shopSelected.setShopCode(shopSelected.getShopCode().toUpperCase().trim());
            shopSelected.setCodePath(codePath.toUpperCase().trim());
            ShopDAO shopDAO = new ShopDAO();
            shopDAO.saveOrUpdate(shopSelected);
//            loadShop(false);
//            getAllShop();
//            buildTreeShop();
            actionTreeShop();

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                    .getSession(false);
            SessionData sessionData = (SessionData) session.getAttribute("username");
            if (type.equals(TYPE_ADD)) {
                LogsConsumer.logInsert(sessionData.getTransEntity(),
                        new Object[] { new Shop(shopSelected) }, resourceLog, Calendar.getInstance().getTime());
            }else{
                LogsConsumer.logUpdate(sessionData.getTransEntity(), new Object[] { shopSelectedOld },
                        new Object[] { new Shop(shopSelected) }, resourceLog, Calendar.getInstance().getTime());
            }
            reset();
//            setSelectedTreeNode(shopParentSelected.getShopId());
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlSave').hide();");
            languageBean._getModel();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header",null,false),
                    languageBean.getMessage("common.action.save.success",null,false));
        } catch (Exception ex) {
            ex.printStackTrace();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getMessage("common.confirm.header",null,false),
                    languageBean.getMessage("common.action.save.error",null,false));
        }
        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }

    private void loadShop(boolean search) {
        Shop st = new Shop();
        st.setShopId(shopParentSelected.getShopId());

        // liStaffs = staffService.getAllWithParams(st, false);
        lazyShop = new LazyShopModel(st, false, true);
    }

    public void beforeSave() {
        FacesMessage message = null;
        if (shopSelected == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header",null,false),
                    languageBean.getMessage("common.acction.beforesave",null,false));
            RequestContext context = RequestContext.getCurrentInstance();
            context.showMessageInDialog(message);
        } else {

            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlSave').show();");
            shopSelectedOld = new Shop(shopSelected);
        }
    }

    public void beforeDelete() {

        FacesMessage message = null;
        ShopDAO shopDAO = new ShopDAO();
        if (shopSelected == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header",null,false),
                    languageBean.getMessage("common.action.beforedelete",null,false));
            RequestContext context = RequestContext.getCurrentInstance();
            context.showMessageInDialog(message);
        } else if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(shopSelected.getShopStatus())){
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header",null,false),
                    languageBean.getMessage("common.action.beforedelete.active",null,false));
            RequestContext context = RequestContext.getCurrentInstance();
            context.showMessageInDialog(message);
        }else{
            List<Shop> lst = shopDAO.getAllShopChildrent(shopSelected.getShopId());
           if(lst!=null && !lst.isEmpty()&&lst.size()>1){
               message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header",null,false),
                       languageBean.getMessage("shop.not.delete.active",null,false));
               RequestContext context = RequestContext.getCurrentInstance();
               context.showMessageInDialog(message);
           }else {
               shopSelectedOld = new Shop(shopSelected);
               RequestContext context = RequestContext.getCurrentInstance();
               context.execute("document.getElementById('frm:confirmButton').click(); ");
           }

        }
    }

    public void delete() {
        FacesMessage message = null;

        try {
            Shop s = getShopSelected();
            s.setShopStatus(0L);
            ShopDAO shopDAO = new ShopDAO();
            shopDAO.saveOrUpdate(s);

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                    .getSession(false);
            SessionData sessionData = (SessionData) session.getAttribute("username");
//            LogsConsumer.logUpdate(sessionData.getTransEntity(), new Object[] { shopSelectedOld },
//                    new Object[] {new Shop(s)}, resourceLog, Calendar.getInstance().getTime());
            LogsConsumer.logDelete(sessionData.getTransEntity(),
                    new Object[] {new Shop(s)},resourceLog,Calendar.getInstance().getTime());

//            loadShop(false);
//            getAllShop();
//            buildTreeShop();
//            setSelectedTreeNode(shopParentSelected.getShopId());
            actionTreeShop();
            reset();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header",null,false),
                    languageBean.getMessage("common.action.delete.success",null,false));
        } catch (Exception e) {
            e.printStackTrace();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getMessage("common.confirm.header",null,false),
                    languageBean.getMessage("common.acction.delete.error",null,false));
        } finally {

        }
        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }

    @Override
    public void validate(FacesContext arg0, UIComponent uiComponent, Object object) throws ValidatorException {
        // TODO Auto-generated method stub
        if (!type.equals(TYPE_SEARCH)) {
            boolean valid = true;
            if (object == null) {
                return;
            }
            FacesMessage msg = null;
            String id = uiComponent.getId();
            valid = true;
            if (id.equals("code")) {
                String value = object.toString();
                if (service.checkExistShopCodeAllStatus(value, shopSelected.getShopId())) {
                    valid = false;
                    msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("shop.message.validate.code", null, false));
                }
            }
            if (id.equals("iplace")) {
                String value = object.toString();
                if (value.trim().isEmpty() || !StringUtils.validatePhoneNumber(value.trim())) {
                    valid = false;
                    msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false), languageBean.getMessage("shop.message.validate.phone", null, false));
                }
            }


            if (!valid) {
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }

    public Shop reset() {
        shopSelected = new Shop();
        return shopSelected;
    }

    public void filterSearch() {
//        System.out.println(shopSelected.getShopCode());
        shopSelected.setShopId(sessionBean.getShop().getShopId());
        // liStaffs = staffService.getAllWithParams(staffSelected, true);
        lazyShop = new LazyShopModel(shopSelected, false, true);
        setSelectedTreeNode(sessionBean.getShop().getShopId());
    }

    public String getStrKeyFilter() {
        return strKeyFilter;
    }

    public void setStrKeyFilter(String strKeyFilter) {
        this.strKeyFilter = strKeyFilter;
    }

    public List<Shop> getListShops() {
        return listShops;
    }

    public void setListShops(List<Shop> listShops) {
        this.listShops = listShops;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public Shop getShopSelected() {
        return shopSelected;
    }

    public void setShopSelected(Shop shopSelected) {
        this.shopSelected = shopSelected;
    }

    public LazyDataModel<Shop> getLazyShop() {
        return lazyShop;
    }

    public Shop getShopParentSelected() {
        return shopParentSelected;
    }

    public void setShopParentSelected(Shop shopParentSelected) {
        this.shopParentSelected = shopParentSelected;
    }

    public LanguageBean getLanguageBean() {
        return languageBean;
    }

    public void setLanguageBean(LanguageBean languageBean) {
        this.languageBean = languageBean;
    }

    public String getTYPE_EDIT() {
        return TYPE_EDIT;
    }

    public void setTYPE_EDIT(String TYPE_EDIT) {
        this.TYPE_EDIT = TYPE_EDIT;
    }

    public String getTYPE_ADD() {
        return TYPE_ADD;
    }

    public void setTYPE_ADD(String TYPE_ADD) {
        this.TYPE_ADD = TYPE_ADD;
    }

    public String getTYPE_SEARCH() {
        return TYPE_SEARCH;
    }

    public void setTYPE_SEARCH(String TYPE_SEARCH) {
        this.TYPE_SEARCH = TYPE_SEARCH;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public String getStrSearchTree() {
        return strSearchTree;
    }

    public void setStrSearchTree(String strSearchTree) {
        this.strSearchTree = strSearchTree;
    }

    public String getResourceLog() {
//        ShopService shopService = new ShopService();
//        Shop shop = shopService.findById(sessionBean.getShop().getShopId());
//        if(shop!=null){
//            sessionBean.setShop(shop);
//        }
//        buildTreeShop();
//        shopParentSelected = sessionBean.getShop();
        return resourceLog;
    }

    public void setResourceLog(String resourceLog) {
        this.resourceLog = resourceLog;
    }

    private List<Boolean> visibleTable = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
    true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
    public void onToggleActiveEtag(ToggleEvent e) {
        visibleTable.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    public List<Boolean> getVisibleTable() {
        return visibleTable;
    }

    public void setVisibleTable(List<Boolean> visibleTable) {
        this.visibleTable = visibleTable;
    }
}