package com.ftu.inventory.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.bo.EquipmentsGroup;
import com.ftu.java.business.EquipmentsGroupService;
import com.ftu.java.business.EquipmentsProfileService;
import com.ftu.sm.bean.GoodsPriceBean;
import com.ftu.sm.bo.GoodsPrice;
import com.ftu.sm.service.GoodsPriceService;
import com.ftu.sm.service.imp.GoodsPriceServiceImp;

import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class GoodsAllBean implements Serializable {

    private EquipmentsGroupService groupService = new EquipmentsGroupService();
    private EquipmentsProfileService goodService = new EquipmentsProfileService();
    private GoodsPriceService priceService = new GoodsPriceServiceImp();
    private List<EquipmentsGroup> listGroups;
    private TreeNode root;
    private TreeNode selectedNode;
    private HashMap<Long, Boolean> mapNodeCollapse;
    private Integer index = 0;
    private String currentTab;

    public GoodsAllBean() {
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        mapNodeCollapse = new HashMap<Long, Boolean>();
        reinit();
    }

    public void reinit() {
        getAllShop();
        buildTreeGroup();
    }

    public void getAllShop() {
        try {
            listGroups = groupService.getAllEquipmentsGroup(null, false, -1, -1);
            listGroups = getTree(listGroups);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildTreeNode(EquipmentsGroup parent, List<EquipmentsProfile> list, TreeNode parentNode) {
        for (EquipmentsProfile gs : list) {
            TreeNode node = new DefaultTreeNode(gs, parentNode);
        }
    }

    private void clearTreeDatas() {
        selectedNode = null;
    }

    public void buildTreeGroup() {
        clearTreeDatas();
        this.root = new DefaultTreeNode("Danh sách loại hàng", null);
        for (EquipmentsGroup g : listGroups) {
            TreeNode node = new DefaultTreeNode(g, this.root);
            List<EquipmentsProfile> listChilds = g.getListGoods();
            if (listChilds != null) {
                if (listChilds.size() > 0) {
                    buildTreeNode(g, listChilds, node);
                }
            }
        }
        if (selectedNode != null) {
            setExpandedTreeNode(selectedNode);
        }
    }

    private List<EquipmentsGroup> getTree(List<EquipmentsGroup> source) {
        List<EquipmentsGroup> list = new ArrayList<EquipmentsGroup>();
        for (EquipmentsGroup g : source) {
            EquipmentsProfile temp = new EquipmentsProfile();
            temp.setEquipmentsGroupId(g.getEquipmentsGroupId());
            List<EquipmentsProfile> listGoods = goodService.getAllProfile(temp, false, -1, -1);
            g.setListGoods(listGoods);
            list.add(g);
        }
        return list;
    }

    public void onNodeExpand(NodeExpandEvent event) {
        Object o = event.getTreeNode().getData();
        if (o instanceof EquipmentsGroup) {
            EquipmentsGroup g = (EquipmentsGroup) o;
            mapNodeCollapse.remove(g.getEquipmentsGroupId());
        }
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        Object o = event.getTreeNode().getData();
        if (o instanceof EquipmentsGroup) {
            EquipmentsGroup g = (EquipmentsGroup) o;
            mapNodeCollapse.put(g.getEquipmentsGroupId(), false);
        }
    }

    public void setSelectedTreeNode(Object g) {
        if (selectedNode != null) {
            selectedNode.setSelected(false);
            selectedNode = null;
        }
        List<TreeNode> lstParent = this.root.getChildren();
        for (TreeNode node : lstParent) {
            if (g instanceof EquipmentsProfile) {
                EquipmentsProfile tmp = (EquipmentsProfile) g;
                List<TreeNode> list = node.getChildren();
                for (TreeNode tn : list) {
                    EquipmentsProfile gt = (EquipmentsProfile) tn.getData();
                    if (gt.getProfileCode().equals(tmp.getProfileCode())) {
                        tn.setSelected(true);
                        selectedNode = tn;
                    }
                }
            } else if (g instanceof EquipmentsGroup) {
                EquipmentsGroup tmp = (EquipmentsGroup) g;
                EquipmentsGroup gg = (EquipmentsGroup) node.getData();
                if (gg.equals(tmp)) {
                    node.setSelected(true);
                    selectedNode = node;
                }
            }
        }
        collapseAllTreeNode(false);
        setExpandedTreeNode(selectedNode);
        loadGoodsPrice(g);
    }

    public void onTabChanged(TabChangeEvent e) {
        try {
            String id = e.getTab().getId();
            currentTab = id;
            if (id.equals("gtab")) {
                loadGoodsPrice(null);
                if (selectedNode == null) {
                    collapseAllTreeNode(true);
                    FacesContext fc = FacesContext.getCurrentInstance();
                    EquipmentsProfileBean goodsBean = (EquipmentsProfileBean) fc.getApplication().evaluateExpressionGet(fc, "#{equipmentsProfileBean}",
                            EquipmentsProfileBean.class);
                    goodsBean.setListGroups(groupService.getAllEquipmentsGroup(null, false, -1, -1));
//                    goodsBean.setGoodSelected(null);
                }
            } else if(id.equals("ggtab")){
                clearSelection(selectedNode);
                selectedNode = null;
                collapseAllTreeNode(false);
            }else{
            	if(selectedNode == null){
            		collapseAllTreeNode(true);
            		FacesContext fc = FacesContext.getCurrentInstance();
                    GoodsPriceBean goodsPriceBean = (GoodsPriceBean) fc.getApplication().evaluateExpressionGet(fc, "#{goodsPriceBean}",
                            GoodsPriceBean.class);
                    goodsPriceBean.setListGoodsPrice(priceService.getAllGoods(null, false, -1, -1));
                    goodsPriceBean.setPriceSelected(null);
            	}
            }
        } catch (Exception ex) {

        }
    }

    public void clearSelection(TreeNode node) {
        if (node != null) {
            node.setSelected(false);
            for (TreeNode t : node.getChildren()) {
                clearSelection(t);
            }
        }
    }

    public void onNodeSelect() {
        Object o = selectedNode.getData();
        collapseAllTreeNode(false);
        setExpandedTreeNode(selectedNode);
        loadGoodsPrice(o);
        changeTab(o);
    }
    
    private void changeTab(Object o){
    	RequestContext context = RequestContext.getCurrentInstance();
    	if(o instanceof EquipmentsProfile){
            context.execute("PF('tabView').select(2)");
    	}else if(o instanceof EquipmentsGroup){
    		context.execute("PF('tabView').select(1)");
    	}
    }

    private void loadGoodsPrice(Object o) {
        FacesContext fc = FacesContext.getCurrentInstance();
        EquipmentsProfileBean goodsBean = (EquipmentsProfileBean) fc.getApplication().evaluateExpressionGet(fc, "#{equipmentsProfileBean}",
                EquipmentsProfileBean.class);
        GoodsPriceBean goodsPriceBean = (GoodsPriceBean) fc.getApplication().evaluateExpressionGet(fc, "#{goodsPriceBean}",
                GoodsPriceBean.class);
        goodsBean.loadGoods();
        if (o != null) {
            //GoodsGroup temp = new GoodsGroup();
        	GoodsPrice temp = new GoodsPrice();
            if (o instanceof EquipmentsProfile) {
                EquipmentsProfile g = (EquipmentsProfile) o;
                //goodsBean.setGoodSelected(g);
                //temp.setGoodsGroupId(((Goods) o).getGoodsGroupId());
                temp.setGoodsId(g.getProfileId());
                temp.setStatus("1");
                goodsPriceBean.setGoods(g);
            } else if (o instanceof EquipmentsGroup) {
                EquipmentsGroup gg = (EquipmentsGroup)o;
                goodsBean.setListGroups(groupService.getAllEquipmentsGroup(gg, false, -1, -1));
            }
            //goodsBean.setListGroups(groupService.getAllGoodsGroup(temp, false, -1, -1));
            goodsPriceBean.setListGoodsPrice(priceService.getAllGoods(temp, false, -1, -1));
//            System.out.println("Size: " + goodsBean.getListGroups().size());
        }
    }

    public void collapseAllTreeNode(boolean collapse) {
        List<TreeNode> list = this.root.getChildren();
        if (list != null && !list.isEmpty()) {
            for (TreeNode tn : list) {
                if (!collapse) {
                    if (tn.isExpanded()) {
                        tn.setExpanded(false);
                    }
                } else {
                    if (!tn.isExpanded()) {
                        tn.setExpanded(true);
                    }
                }
            }
        }
    }

    public void setExpandedTreeNode(TreeNode treeNode) {
        if (treeNode != null) {
            TreeNode parent = treeNode.getParent();
            if (parent != null) {
                if (!parent.isExpanded()) {
                    parent.setExpanded(true);
                }
            }

            if (!treeNode.isExpanded()) {
                treeNode.setExpanded(true);
            }
        }
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

    public List<EquipmentsGroup> getListGroups() {
        return listGroups;
    }

    public void setListGroups(List<EquipmentsGroup> listGroups) {
        this.listGroups = listGroups;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(String currentTab) {
        this.currentTab = currentTab;
    }

}
