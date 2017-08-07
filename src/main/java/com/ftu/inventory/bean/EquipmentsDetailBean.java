/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.bussiness.ActionAuditService;
import com.ftu.inventory.bussiness.EquipmentHistoryDetailService;
import com.ftu.inventory.bussiness.EquipmentHistoryService;
import com.ftu.inventory.bussiness.EquipmentsDetailService;
import com.ftu.java.bo.LazyEquipmentsModel;
import com.ftu.java.business.EquipmentsGroupService;
import com.ftu.java.business.EquipmentsProfileService;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Position;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.staff.bussiness.PositionService;
import com.ftu.staff.bussiness.ShopService;
import com.ftu.staff.bussiness.StaffService;
import com.ftu.utils.ComponentUtils;
import com.ftu.utils.DateTimeUtils;
import com.ftu.utils.StringUtils;
import com.ftu.ws.BusinessService;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.Visibility;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author E5420
 */
@ManagedBean
@ViewScoped
public class EquipmentsDetailBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;
    @ManagedProperty(value = "#{languageBean}")
    private LanguageBean languageBean;
    private List<Shop> listShop;
    private List<Shop> listShopALL;
    private Long shopId;
    private List<EquipmentsProfile> listEquipments;
    private Long equipmentsId;
    private Long equipmentsStatusId;
    private List<ApDomain> listStockStatus;
    private Long stockStatusId;
    private String fromSerial;
    private String toSerial;
    private LazyDataModel<EquipmentsDetail> lsData;
    private List<Provider> listProvider;
    private Long providerId;
    private StockGoodsInvSerial etag = new StockGoodsInvSerial();
    private EtagDetail etadDetail = new EtagDetail();
    private List<TransactionAction> listTrans = new ArrayList<>();
    private List<GoodsStatusTransSerial> listStatusTrans = new ArrayList<>();
    List<ApDomain> listTransStatus;
    List<ApDomain> listTransType;
    List<ApDomain> listActionType;
    private Long equipmentsGroupId;
    List<EquipmentsGroup> lsEquipmentsGroup;
    boolean fist = true;
    private String equipments;
    private EquipmentsDetail equipmentsDetail = new EquipmentsDetail();
    private StaffService service = new StaffService();
    private BusinessService businessService = new BusinessService();
    private List<Staff> staffs = new ArrayList<>();
    //huy
    private String createShop;
    private EquipmentsDetail equipmentsDetailSelected = new EquipmentsDetail();
    LazyDataModel<EquipmentHistory> equipmentHistories;
    private EquipmentHistory equipHistorySelected = new EquipmentHistory();
    private List<Position> positionAll = new ArrayList<Position>();
    private PositionService positionService = new PositionService();
    private List<Shop> lstShop = new ArrayList<Shop>();
    private ShopService shopService = new ShopService();
    private List<TransactionAction> lstTransactionAction = new ArrayList<>();
    List<Staff> lstStaff = new ArrayList<>();
    private List<Position> lstPostion = new ArrayList<>();
    private ActionAuditService actionAuditService = new ActionAuditService();
    private EquipmentHistoryDetailService equipmentHistoryDetailService = new EquipmentHistoryDetailService();
    private EquipmentsDetailService equipmentsDetailService = new EquipmentsDetailService();
    private List<Shop> lstShopHis = new ArrayList<Shop>();
    private List<EquipmentsDetail> lstEquipmentsDetailSelected = new ArrayList<EquipmentsDetail>();
    private EquipmentsDetail equipmentsDetailShow = new EquipmentsDetail();
    private Boolean disableShop = false;
    private Boolean disableActionType = false;
    private Boolean disableGooodStatus = false;
    private EquipmentsProfileService equipmentsProfileService = new EquipmentsProfileService();
    private EquipmentHistoryService equipmentHistoryService = new EquipmentHistoryService();
    private EquipmentsGroupService equipmentsGroupService = new EquipmentsGroupService();
    private Boolean disableBeforeAdd = false;
    
    @PostConstruct
    public void init() {
    	sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
        listStockStatus = sessionBean.getService().getListSearchStocktatus();
        listShop = new ArrayList<>();//        Shop allShop = new Shop(-1l);
//        allShop.setShopName("Tất cả");
//        listShop.add(allShop);
        listShopALL = sessionBean.getService().findAllShop();
        _getListShop(sessionBean.getService().getAllShop(null));
        listProvider = sessionBean.getLsProvider();
        listTransStatus = sessionBean.getService().getListTransStatus();
        listTransType = sessionBean.getService().getListTransType();
        lsEquipmentsGroup = sessionBean.getLsgGroup();
//        changeEquipmentsGroup();
//        comment
//        search();

        shopId = sessionBean.getShop().getShopId();
        createShop = sessionBean.getShop().getShopName();
        
        staffs = service.findAll();
        positionAll = positionService.getAll();
        
        lstTransactionAction = businessService.searchTranByEDtail(
        		new TransactionAction(
        				InventoryConstanst.TransactionActionCode.EMEX, 
        				null, 
        				null, 
        				shopId, 
        				null, 
        				null, 
        				null));
        		
        lstStaff = businessService.getAllStaffByTransactionAction();
        Shop shop = new Shop();
        shop.setShopId(sessionBean.getShop().getShopId());
        lstShopHis = shopService.getListShopByParentId(shop, false, -1, -1);
        //sessionBean.getService().getListShop(sessionBean.getShop().getShopId(), null);//shopService.getAllShopPosition(sessionBean.getShop().getShopId());
        changeEquipmentsGroup();
        search();
        listEquipments = sessionBean.getLsEquipments();
    }

    public void setPositionCode(Long positionId, EquipmentHistory equipmentHistory){
        if (positionAll ==  null || positionId == null) return ;
        for (Position position:positionAll) {
            if(positionId.equals(position.getPositionId())){
                equipmentHistory.setPositionCode(position.getPositionCode());
                equipmentHistory.setLanCode(position.getLaneCode());
            }
        }
    }
    public void createShopSelect() {
        if (listShop != null) {
            if(createShop.equals("Tất cả")){
                shopId = null;
                equipmentsDetail.setShopId(null);
            }else{
                for (Shop shop : listShop) {
                    if (shop.getShopName().equals(createShop)) {
                        shopId = shop.getShopId();
                        equipmentsDetail.setShopId(shop.getShopId());
                        break;
                    }
                }
            }

        }
    }

    public void createTransactionSelect() {
        if (lstTransactionAction != null) {
            if(equipmentsDetail.getTransactionActionCode().equals("Tất cả")){
                equipmentsDetail.setTransactionActionID(null);
            }else
            for (TransactionAction obj : lstTransactionAction) {
                if (obj.getTransactionActionCode().equals(equipmentsDetail.getTransactionActionCode())) {
                    equipmentsDetail.setTransactionActionID(obj.getTransactionActionId());
                    break;
                }
            }
        }
    }

    public void createStaffSelect() {
        if (lstStaff != null) {
            if(equipmentsDetail.getStaffName().equals("Tất cả")){
                equipmentsDetail.setStaffId(null);
            }else
            for (Staff obj : lstStaff) {
                String codeName = obj.getStaffCode() + " | " + obj.getStaffName();
                if (codeName.equals(equipmentsDetail.getStaffName())) {
                    equipmentsDetail.setStaffId(obj.getStaffId());
                    break;
                }
            }
        }
    }

    public List<String> completeCreateShop(String query) {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                    .getSession(false);
            SessionData data = (SessionData) session.getAttribute("username");
            List<String> filteredThemes = new ArrayList<String>();
            createShop = "Tất cả";
            filteredThemes.add("Tất cả");
            for (int i = 0; i < listShop.size(); i++) {
                Shop skin = listShop.get(i);
                if (skin.getShopName().toLowerCase().contains(query.toLowerCase())) {
                    filteredThemes.add(skin.getShopName());
                }
            }
            return filteredThemes;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<String> completeStaffCodeName(String query) {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                    .getSession(false);
            SessionData data = (SessionData) session.getAttribute("username");
            List<String> filteredThemes = new ArrayList<String>();
            equipmentsDetail.setStaffName("Tất cả");
            filteredThemes.add("Tất cả");
            equipmentsDetail.setStaffId(null);
            for (int i = 0; i < lstStaff.size(); i++) {
                Staff skin = lstStaff.get(i);
                String codeName = skin.getStaffCode() + " | " + skin.getStaffName();
                if (skin.getStaffCode().toLowerCase().contains(query.toLowerCase())
                        || skin.getStaffName().toLowerCase().contains(query.toLowerCase())) {
                    filteredThemes.add(codeName);
                }
                if (codeName.toLowerCase().equals(query.toLowerCase())) {
                    equipmentsDetail.setStaffName(codeName);
                    equipmentsDetail.setStaffId(skin.getStaffId());
                }
            }
            return filteredThemes;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<String> completeTransactionCode(String query) {
        try {
//            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
//                    .getSession(false);
//            SessionData data = (SessionData) session.getAttribute("username");
            List<String> filteredThemes = new ArrayList<String>();
            equipmentsDetail.setTransactionActionCode("Tất cả");
            filteredThemes.add("Tất cả");
            for (int i = 0; i < lstTransactionAction.size(); i++) {
                TransactionAction skin = lstTransactionAction.get(i);
                if (skin.getTransactionActionCode().toLowerCase().contains(query.toLowerCase())) {
                    filteredThemes.add(skin.getTransactionActionCode());
                }
                if (skin.getTransactionActionCode().toLowerCase().equals(query.toLowerCase())) {

                    equipmentsDetail.setTransactionActionID(skin.getTransactionActionId());
                }
            }
            return filteredThemes;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void _getListShop(Shop s) {
        if (listShop.size() == 0) {
//    		Shop allShop = new Shop(-1l);
//            allShop.setShopName("Tất cả");
//            listShop.add(0,allShop);
        }
        listShop.add(s);
        if (s.getChildShops() != null) {
            for (Shop sub : s.getChildShops()) {
                _getListShop(sub);
            }
        }

//        if(listShop != null && !listShop.isEmpty()){
//			createShop = listShop.get(0).getShopName();
//			shopId = listShop.get(0).getShopId();
//		}

    }

    public void changeEquipmentsGroup() {
//        if (lsEquipmentsGroup != null && !lsEquipmentsGroup.isEmpty() && (equipmentsGroupId == null || equipmentsGroupId == 0L)) {
//            setEquipmentsGroupId(lsEquipmentsGroup.get(0).getEquipmentsGroupId());
//            equipmentsDetail.setEquipmentsGroupId(equipmentsGroupId);
//        }
        if (equipmentsDetail != null && equipmentsDetail.getEquipmentsGroupId() != null) {
            equipmentsGroupId = equipmentsDetail.getEquipmentsGroupId();
            List<EquipmentsProfile> lsg = sessionBean.getLsEquipments();
            listEquipments = new ArrayList<>();
            if (lsg != null && !lsg.isEmpty()) {
                for (EquipmentsProfile g : lsg) {
                    if (equipmentsGroupId.equals(g.getEquipmentsGroupId())) {
                        listEquipments.add(g);
                    }
                }
            }
        }
        if(equipmentsDetail.getEquipmentsGroupId()==null&&listEquipments!=null){
            listEquipments = sessionBean.getLsEquipments();
        }
        equipmentsId = null;
        equipmentsDetail.setEquipments("Tất cả");
        equipmentsDetail.setEquipmentsProfileId(null);

    }

    public void changShopDlg() {
        if (equipHistorySelected.getShopIdHis() != null) {
            lstPostion = positionService.getPositionNotSetting(equipHistorySelected.getShopIdHis());
            List<Long> positionIds = new ArrayList<>();
            if(lstEquipmentsDetailSelected!=null && !lstEquipmentsDetailSelected.isEmpty()){
                for (EquipmentsDetail equip:lstEquipmentsDetailSelected) {
                    if(equip.getEquipmentHistories()!=null){
                        for (EquipmentHistory equipChi:equip.getEquipmentHistories()) {
                            positionIds.add(equipChi.getPositionId());
                        }
                    }
                }
            }
            List<Position> lstPostionRemove = new ArrayList<>();
            if(lstPostion!=null && !lstPostion.isEmpty() && positionIds!=null && !positionIds.isEmpty()){
                for (Position position: lstPostion){
                    for (Long id:positionIds){
                        if(position.getPositionId().equals(id)){
                            lstPostionRemove.add(position);
                        }
                    }

                }
                lstPostion.removeAll(lstPostionRemove);
            }
        }
    }

    public void equipmentsSelect() {
        if (listEquipments != null) {
            if(equipmentsDetail.getEquipments().equals("Tất cả")|| equipmentsDetail.getEquipments().isEmpty()){
                equipmentsId = null;
                equipmentsDetail.setEquipmentsProfileId(null);
            }else
            for (EquipmentsProfile g : listEquipments) {
                if ((g.getProfileCode() + " | " + g.getProfileName()).equals(equipmentsDetail.getEquipments())) {
                    equipmentsId = g.getProfileId();
                    equipmentsDetail.setEquipmentsProfileId(g.getProfileId());
                    break;
                }
            }
        }
    }

    public List<String> completeEquipments(String gs) {
        equipments = gs;
//        equipmentsDetail.setEquipments(gs);
        List<String> rs = new ArrayList<>();
        rs.add("Tất cả");
        equipmentsDetail.setEquipments("Tất cả");
        equipmentsDetail.setEquipmentsProfileId(null);
        if (listEquipments != null && !listEquipments.isEmpty()) {
            for (EquipmentsProfile g : listEquipments) {
                String name = g.getProfileCode() + " | " + g.getProfileName();
                if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(g.getManagementType())){
                    continue;
                }
                if (name.toLowerCase().contains(gs.toLowerCase())) {
                    rs.add(g.getProfileCode() + " | " + g.getProfileName());
                }
            }
        }
        return rs;
    }

    public String getShopName(Long shopId) {
        if (listShopALL != null) {
            for (Shop d : listShopALL) {
                if (d.getShopId().equals(shopId)) {
                    return d.getShopName();
                }
            }
        }
        return "";
    }

    private String getTransStatus(Long taId) {
        if (listTransStatus != null) {
            for (ApDomain d : listTransStatus) {
                if (d.getValue().equals(taId)) {
                    return d.getName();
                }
            }
        }
        return "";
    }

    private String getTransType(Long taId) {
        if (listTransType != null) {
            for (ApDomain d : listTransType) {
                if (d.getValue().equals(taId)) {
                    return d.getName();
                }
            }
        }
        return "";
    }

    public void sort() {
        Collections.sort(listStatusTrans, new Comparator<GoodsStatusTransSerial>() {
                    @Override
                    public int compare(GoodsStatusTransSerial t, GoodsStatusTransSerial t1) {
//                int rs = t.getProviderId().intValue() - t1.getProviderId().intValue();
//                return rs == 0 ? new Integer(t.getSerial()) - new Integer(t1.getSerial()) : rs;
                        return t1.getSerial().compareTo(t.getSerial());
                    }
                }
        );
    }

    public void view(SelectEvent event) {
        EquipmentsDetail obj = (EquipmentsDetail) event.getObject();
        if (obj == null || obj.getEquipmentsProfileId() == null) {
            return;
        }
        disableShop = false;
        if(obj.getStockStatusId().toString().equals(InventoryConstanst.StockStatus.INSTOCK)){
            disableShop = true;
        }
//        equipHistorySelected
//        equipmentHistories = new LazyEquipmentHistoryModel(null, obj.getId());
//        setDataHistory();
        if(obj.getShopId() == null || !obj.getShopId().equals(sessionBean.getShop().getShopId())){
            FacesMessage message = null;
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
                    languageBean.getMessage("equipment.not.shop.edit", null, false));
            RequestContext context = RequestContext.getCurrentInstance();
            context.showMessageInDialog(message);
            disableBeforeAdd = true;
        }
        removeEquipDetailEmpty();
        equipmentsDetailShow  =  getEquipmentsDetailShow(obj.getId(), obj);
    }
    public EquipmentsDetail getEquipmentsDetailShow(Long equipmentsId, EquipmentsDetail equipmentsDetail){
        disableBeforeAdd = false;
        if(lstEquipmentsDetailSelected==null || lstEquipmentsDetailSelected.isEmpty()){
            lstEquipmentsDetailSelected = new ArrayList<>();
            EquipmentHistory hisSearch = new EquipmentHistory();
            hisSearch.setEquipmentId(equipmentsId);
//            List<EquipmentHistory> lst = equipmentHistoryService.searchByEquipmentId(hisSearch, null, null);
            equipmentsDetail.setEquipmentHistories(new ArrayList<EquipmentHistory>());
//            if(lst !=null){
//                for (EquipmentHistory obj:lst) {
//                    obj.setMaintancePerio(equipmentsDetail.getMaintancePeriod());
//                    obj.setLifeCycle(equipmentsDetail.getLifeCycle());
//                    obj.setEquipmentId(equipmentsDetail.getId());
//                    equipmentsDetail.getEquipmentHistories().add(obj);
//                }
//            }
            lstEquipmentsDetailSelected.add(equipmentsDetail);
            return equipmentsDetail;
        }
        for (EquipmentsDetail obj:lstEquipmentsDetailSelected) {
            if(obj.getId().equals(equipmentsDetail.getId())){
                disableBeforeAdd = true;
                return obj;
            }
        }
        EquipmentHistory hisSearch = new EquipmentHistory();
        hisSearch.setEquipmentId(equipmentsId);
//        List<EquipmentHistory> lst = equipmentHistoryService.searchByEquipmentId(hisSearch, null, null);
        equipmentsDetail.setEquipmentHistories(new ArrayList<EquipmentHistory>());
//        if(lst !=null){
//            for (EquipmentHistory obj:lst) {
//                obj.setMaintancePerio(equipmentsDetail.getMaintancePeriod());
//                obj.setLifeCycle(equipmentsDetail.getLifeCycle());
//                obj.setEquipmentId(equipmentsDetail.getId());
//                equipmentsDetail.getEquipmentHistories().add(obj);
//            }
//        }
        lstEquipmentsDetailSelected.add(equipmentsDetail);
        return equipmentsDetail;
    }
    public void removeEquipDetailEmpty(){
        List<EquipmentsDetail> lsDelete = new ArrayList<>();
        for (EquipmentsDetail obj:lstEquipmentsDetailSelected) {
            if (obj.getEquipmentHistories() == null || obj.getEquipmentHistories().isEmpty()){
                lsDelete.add(obj);
            }
        }
        if(!lsDelete.isEmpty()){
            lstEquipmentsDetailSelected.removeAll(lsDelete);
        }
    }

    public void search() {
//        if(equipmentsId == null){
//            FacesMessage message = null;
//            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
//                    languageBean.getMessage("equipment.not.empty.search", null, false));
//            RequestContext context = RequestContext.getCurrentInstance();
//            context.showMessageInDialog(message);
//            return;
//        }
        if(equipmentsDetail.getEquipmentsGroupId() != null){
            Long size  = equipmentsGroupService.checkGroupIsSerial(equipmentsDetail.getEquipmentsGroupId());
            if(size < 1L){
                FacesMessage message = null;
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
                        languageBean.getMessage("equipment.not.serial.search", null, false));
                RequestContext context = RequestContext.getCurrentInstance();
                context.showMessageInDialog(message);
                return;
            }
        }
        if (!fist) {
            ComponentUtils cu = new ComponentUtils();
            DataTable dt = (DataTable) cu.findComponent("dtSearchEtagSerial");
            if (dt != null) {
                dt.setFirst(0);
            }
        }
        if ((createShop == null) || createShop.equals("")) {
            shopId = null;
        }
        shopId = sessionBean.getShop().getShopId();
        equipmentsDetail.setUpdateSetupProfile(true);
        equipmentsDetail.setShopId(shopId);
        fist = false;
        lsData = new LazyEquipmentsModel(fromSerial, getToSerial(), equipmentsDetail, equipmentsGroupId, listShop);

    }

    public void beforeAdd() {
        FacesMessage message = null;
        if (equipmentsDetailSelected == null || equipmentsDetailSelected.getId() == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
                    languageBean.getMessage("common.acction.beforesave", null, false));
            RequestContext context = RequestContext.getCurrentInstance();
            context.showMessageInDialog(message);
        } else {

            equipmentsDetailSelected = equipmentsDetailShow;
//            setDataHistory();
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("document.getElementById('frm:beforeAdd').click(); ");
        }
    }
//    public List<ApDomain> getListActionType(){
//        List<ApDomain> lstApAll = new ArrayList<>();
//        lstApAll.addAll(sessionBean.getListActionType());
//                equipmentHistories = new LazyEquipmentHistoryModel(null, equipmentsDetailShow.getId());
//        if((equipmentHistories!=null && equipmentHistories.getRowCount() > 0)
//                ||  (equipmentsDetailShow.getEquipmentHistories() != null && !equipmentsDetailShow.getEquipmentHistories().isEmpty())){
//            for (ApDomain ap :  sessionBean.getListActionType()){
//                if (InventoryConstanst.ACTION_TYPE.TYPE_1L.equals(ap.getValue())){
//                    lstApAll.remove(ap);
//                }
//            }
//        } else if((equipmentHistories==null || equipmentHistories.getRowCount() == 0)
//                ||  equipmentsDetailShow.getEquipmentHistories().isEmpty()){
//            for (ApDomain ap :  sessionBean.getListActionType()){
//                if (!InventoryConstanst.ACTION_TYPE.TYPE_1L.equals(ap.getValue())){
//                    lstApAll.remove(ap);
//                }
//            }
//        }
//        return lstApAll;
//    }

    public void beforeSave() {

        FacesMessage message = null;
        if (equipmentsDetailSelected == null || equipmentsDetailSelected.getId() == null||equipHistorySelected == null || equipHistorySelected.getEquipmentHistoryId() == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
                    languageBean.getMessage("common.acction.beforesave", null, false));
            RequestContext context = RequestContext.getCurrentInstance();
            context.showMessageInDialog(message);
        } else {
            setDataHistory();
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlSave').show();");
        }
    }

    public void delete(EquipmentHistory equipmentHistory) {
        if(equipmentHistory.getEquipmentHistoryId()!=null){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
                    languageBean.getMessage("save.history.exit.not.delete", null, false));
            RequestContext context = RequestContext.getCurrentInstance();
            context.showMessageInDialog(message);
            return;
        }
        equipmentsDetailShow.getEquipmentHistories().remove(equipmentHistory);
        removeEquipDetailEmpty();
        disableBeforeAdd = false;
    }
    public String getActionTypeName(Long type) {
        try {
            if (type == null) {
                return "";
            }
            for (ApDomain ap : sessionBean.getListActionType()) {
                if (type.equals(ap.getValue())) {
                    return ap.getName();
                }
            }
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public void validate(FacesContext arg0, UIComponent uiComponent, Object object) throws ValidatorException {
        if (true) {
            boolean valid = true;
            if (object == null) {
                return;
            }
            FacesMessage msg = null;
            String id = uiComponent.getId();
            valid = true;
            if (id.equals("actionDateDlg")) {
                try {
                    SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy");
                    String strDate = sm.format(new Date());
                    Date  dt = sm.parse(strDate);
                    Date value = (Date) object;
                    if (dt.compareTo(value) > 0) {
                        valid = false;
                        msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
                                languageBean.getMessage("equipmentsDetailBean.validator.actionDate.notThan", null, false));
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
            if (id.equals("referenceIdDlg")) {
                String value = object.toString().trim();
                if (value != null && !value.trim().isEmpty() && (!StringUtils.isNumberLong̣̣(value)||Long.parseLong(value.trim()) < 0)) {
                    valid = false;
                    msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
                            languageBean.getMessage("equipmentsDetailBean.validator.referenceId.number", null, false));
                }
            }
            if (id.equals("specificationDlg")) {
                String value = object.toString().trim();
                if(value!=null && !value.isEmpty()){
                    if(value.getBytes(Charset.forName("UTF-8")).length > 2000){
                        valid = false;
                        msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
                                languageBean.getMessage("equipmentsDetailBean.validator.maxleng.2000", null, false));
                    }
                }

//                List<String> characters=new ArrayList<String>();
//                Pattern pat = Pattern.compile("\\p{L}\\p{M}*");
//                Matcher matcher = pat.matcher(value);
//                while (matcher.find()) {
//                    characters.add(matcher.group());
//                }
//                valid = false;
//                msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
//                        languageBean.getMessage("String length: " + characters.size(), null, false));
                // Test if we have the right characters and length
//                System.out.println(characters);
//                System.out.println("String length: " + characters.size());
            }
            if (id.equals("maintancePerioDlg")) {
                String value = object.toString().trim();
                if (value != null && !value.trim().isEmpty() && !StringUtils.isNumberLong̣̣(value)) {
                    valid = false;
                    msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
                            languageBean.getMessage("equipmentsDetailBean.validator.maintancePerio.number", null, false));
                }
            }

            if (id.equals("lifeCycleDlg")) {
                String value = object.toString().trim();
                if (value != null && !value.trim().isEmpty() && !StringUtils.isNumberLong̣̣(value)) {
                    valid = false;
                    msg = new FacesMessage(languageBean.getMessage("common.confirm.header", null, false),
                            languageBean.getMessage("equipmentsDetailBean.validator.lifeCycle.number", null, false));
                }
            }

            if (!valid) {
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }
    public void beforeSaveHistory() {

        FacesMessage message = null;
        if (equipmentsDetailShow == null || equipmentsDetailShow.getEquipmentHistories() == null || equipmentsDetailShow.getEquipmentHistories().isEmpty()) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
                    languageBean.getMessage("save.history.empty", null, false));
            RequestContext context = RequestContext.getCurrentInstance();
            context.showMessageInDialog(message);
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("document.getElementById('frm:confirmButton').click(); ");
        }
    }
    public List<ApDomain> getListGoodStatus() {
        List<ApDomain> lst = new ArrayList<>();
        if(equipmentsDetailShow == null || equipmentsDetailShow.getStockStatusId() == null)
        {return sessionBean.getListStatus();}
        lst.addAll(sessionBean.getListStatus());
        if(equipmentsDetailShow.getStockStatusId().toString().equals(InventoryConstanst.StockStatus.USED)){
            for (ApDomain ap:lst) {
                if(InventoryConstanst.GoodsStatus.NOMAL.equals(ap.getValue())){
                    lst.remove(ap);
                    break;
                }
            }
        }
        return lst;
    }

    private void setDataHistory() {
        equipHistorySelected.setPositionId(equipmentsDetailShow.getPositionId());
        equipHistorySelected.setEquipmentStatus(equipmentsDetailShow.getStatus());
        equipHistorySelected.setMaintancePerio(equipmentsDetailShow.getMaintancePeriod());
//        equipHistorySelected.setSpecification(equipmentsDetailShow.getEquipmentSpecification());
        equipHistorySelected.setLifeCycle(equipmentsDetailShow.getLifeCycle());
        equipHistorySelected.setActionDate(new Date());
        EquipmentsProfile profile = equipmentsProfileService.findByProfileId(equipmentsDetailShow.getEquipmentsProfileId());
//        equipHistorySelected.setSpecification(profile.getSpecification());
        equipHistorySelected.setSpecification(equipmentsDetailShow.getEquipmentSpecification());
        if(equipmentsDetailShow.getPositionId()!=null){
            Position p = positionService.findById(equipmentsDetailShow.getPositionId());
            equipHistorySelected.setShopIdHis(p.getShopId());
        }
        if (equipHistorySelected.getShopIdHis()!=null) {
            lstPostion = positionService.findByShopIdActive(equipHistorySelected.getShopIdHis());
        }
        if (equipHistorySelected.getEquipmentHistoryId() != null) {
            ActionAudit actionAudit = actionAuditService.findByreferenceId(equipHistorySelected.getEquipmentHistoryId());
            if(actionAudit==null) return;
            equipHistorySelected.setActionType(actionAudit.getActionType());
            equipHistorySelected.setActionDate(actionAudit.getActionDatetime());

        }
        disableActionType = false;
        disableGooodStatus = false;
        listActionType = new ArrayList<>();
        listActionType.addAll(sessionBean.getListActionTypeSetUp());
        if(InventoryConstanst.StockStatus.INSTOCK.equals(equipmentsDetailShow.getStockStatusId()+"")){
            equipHistorySelected.setActionType(InventoryConstanst.ACTION_TYPE.TYPE_4L);
            disableActionType = true;
            disableShop = true;
        }else if(equipmentsDetailShow.getStockStatusId().toString().equals(InventoryConstanst.StockStatus.EX_USED)){

            disableActionType = true;
//            lstPostion = positionService.getPositionNotSetting(null);
            lstPostion =  positionService.getPositionNotSetting(sessionBean.getShop().getShopId());
            List<Long> positionIds = new ArrayList<>();
            if(lstEquipmentsDetailSelected!=null && !lstEquipmentsDetailSelected.isEmpty()){
                for (EquipmentsDetail equip:lstEquipmentsDetailSelected) {
                    if(equip.getEquipmentHistories()!=null){
                        for (EquipmentHistory equipChi:equip.getEquipmentHistories()) {
                            positionIds.add(equipChi.getPositionId());
                        }
                    }
                }
            }
            List<Position> lstPostionRemove = new ArrayList<>();
            if(lstPostion!=null && !lstPostion.isEmpty() && positionIds!=null && !positionIds.isEmpty()){
                for (Position position: lstPostion){
                    for (Long id:positionIds){
                        if(position.getPositionId().equals(id)){
                            lstPostionRemove.add(position);
                        }
                    }

                }
                lstPostion.removeAll(lstPostionRemove);
            }
            if(!InventoryConstanst.GoodsStatus.ERROR.equals(equipHistorySelected.getEquipmentStatus())){
                equipHistorySelected.setEquipmentStatus(InventoryConstanst.GoodsStatus.USED);
                equipHistorySelected.setActionType(InventoryConstanst.ACTION_TYPE.TYPE_1L);
            }else{
                equipHistorySelected.setActionType(InventoryConstanst.ACTION_TYPE.TYPE_4L);
                disableShop = true;
            }
            disableGooodStatus = true;
        } else if(equipmentsDetailShow.getStockStatusId().toString().equals(InventoryConstanst.StockStatus.USED)){
            for (ApDomain ap:listActionType) {
                if(InventoryConstanst.ACTION_TYPE.TYPE_1L.equals(ap.getValue())){
                    listActionType.remove(ap);
                    break;
                }
            }
            disableShop = true;
        }
        if(equipmentsDetailSelected.getId()!=null){
            EquipmentHistory oldValue = equipmentHistoryService.findByEquipmentId(equipmentsDetailSelected.getId());
            if(oldValue!=null){
                equipHistorySelected.setNote(oldValue.getNote());
            }
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("frmDialog:dlSave");
    }
    
    public void save() {
        equipHistorySelected.setRowKey(equipmentsDetailShow.getId()+"_"+ (new SimpleDateFormat("hh_mm_ss").format(new Date())));
        setPositionCode(equipHistorySelected.getPositionId(), equipHistorySelected);
        equipmentsDetailShow.getEquipmentHistories().add(equipHistorySelected);

        removeEquipDetailEmpty();
        disableBeforeAdd = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dlSave').hide()");
        context.update("frm:dtSearchEtagSerial");
        context.update("frm:tblEquipmentHistory");
    }
    
    public void insertHistory() throws Exception{
        try {
            ActionAudit actionAudit;
            equipHistorySelected.setEquipmentId(equipmentsDetailSelected.getId());
            if (equipHistorySelected.getEquipmentHistoryId() == null) {
                actionAudit = new ActionAudit();
            } else {
                actionAudit = actionAuditService.findByreferenceId(equipHistorySelected.getEquipmentHistoryId());
                if (actionAudit == null) {
                    actionAudit = new ActionAudit();
                }
            }
            EquipmentHistory oldValue = equipmentHistoryService.findByEquipmentId(equipHistorySelected.getEquipmentId());
            equipHistorySelected.setEquipmentId(equipmentsDetailSelected.getId());
            EquipmentsDetail detailOld = new EquipmentsDetail();
            detailOld.setStatus(equipmentsDetailSelected.getStatus());
            detailOld.setPositionId(equipmentsDetailSelected.getPositionId());
            detailOld.setEquipmentSpecification(equipmentsDetailSelected.getEquipmentSpecification());
            detailOld.setMaintancePeriod(equipmentsDetailSelected.getMaintancePeriod());
            detailOld.setLifeCycle(equipmentsDetailSelected.getLifeCycle());

            equipHistorySelected.setCreatedDatetime(DateTimeUtils.getDate());
            // TODO: 12/20/2016
            equipHistorySelected.setStaffId(sessionBean.getStaff().getStaffId());
            equipHistorySelected.setShopId(sessionBean.getStaff().getShopId());
            equipmentHistoryService.saveOrUpdate(equipHistorySelected);

//            actionAudit.setActionDatetime(equipHistorySelected.getActionDate());
            actionAudit.setActionDatetime(DateTimeUtils.getDate());
            actionAudit.setActionType(equipHistorySelected.getActionType());
            actionAudit.setReferenceId(equipHistorySelected.getEquipmentHistoryId());
            actionAuditService.saveOrUpdate(actionAudit);

            equipmentsDetailSelected.setPositionId(equipHistorySelected.getPositionId());
            equipmentsDetailSelected.setStatus(equipHistorySelected.getEquipmentStatus());
            equipmentsDetailSelected.setMaintancePeriod(equipHistorySelected.getMaintancePerio());
            equipmentsDetailSelected.setEquipmentSpecification(equipHistorySelected.getSpecification());
            equipmentsDetailSelected.setLifeCycle(equipHistorySelected.getLifeCycle());
//            if(equipmentsDetailSelected.getWarrantyCount()==null || equipmentsDetailSelected.getWarrantyCount().equals(0L)){
//                equipmentsDetailSelected.setWarrantyCount(1L);
//            }else {
//                equipmentsDetailSelected.setWarrantyCount(equipmentsDetailSelected.getWarrantyCount() + 1L);
//            }
//            equipmentsDetailSelected.setLastUsedDate(new Date());

            equipmentsDetailService.saveOrUpdate(equipmentsDetailSelected);

            String[] lstColumn = {"POSITION_ID","EQUIPMENT_STATUS","REFERENCE_ID",
                    "SPECIFICATION","MAINTANCE_PERIOD","LIFE_CYCLE","NOTE"};
            List<EquipmentHistoryDetail> lstHistoryDetail = new ArrayList<>();
//            equipmentHistoryDetailService.deleteByEquipmentHistoryId(equipHistorySelected.getEquipmentHistoryId());
//            EquipmentHistory oldObjValue = equipmentHistoryService.findById(equipHistorySelected.getEquipmentId());

            for (int i=0; i<lstColumn.length;i++){
                EquipmentHistoryDetail obj = new EquipmentHistoryDetail();
                obj.setEquipmentHistoryId(equipHistorySelected.getEquipmentHistoryId());
                try {
                    switch (lstColumn[i]) {
                        case "POSITION_ID":
                            obj.setFeildName(InventoryConstanst.ACTION_AU_DETAIL.POSITION_ID);
                            if(equipHistorySelected.getPositionId()==null){
                                obj.setNewValue("");
                            }else {
                                Position p  = positionService.findById(equipHistorySelected.getPositionId());
                                obj.setNewValue(p.getPositionName());
                            }
                            if(detailOld!=null)
                                if(detailOld.getPositionId()==null){
                                    obj.setOldValue("");
                                }else {
                                    Position p  = positionService.findById(detailOld.getPositionId());
                                    obj.setOldValue(p.getPositionName());
                                }
                            break;
                        case "EQUIPMENT_STATUS":
                            obj.setFeildName(InventoryConstanst.ACTION_AU_DETAIL.EQUIPMENT_STATUS);
                            obj.setNewValue(equipHistorySelected.getEquipmentStatus()==null ? "" :
                                    sessionBean.getEquipStatusName(equipHistorySelected.getEquipmentStatus()+"")+"");
                            if(detailOld!=null) {
                                obj.setOldValue(detailOld.getStatus() == null ?
                                        sessionBean.getEquipStatusName(InventoryConstanst.GoodsStatus.NOMAL.toString()) :
                                        sessionBean.getEquipStatusName(detailOld.getStatus() + ""));
                            }else {
                                obj.setOldValue(sessionBean.getEquipStatusName(InventoryConstanst.GoodsStatus.NOMAL.toString()));
                            }
                            break;
                        case "REFERENCE_ID":
                            obj.setFeildName(InventoryConstanst.ACTION_AU_DETAIL.REFERENCE_ID);
                            obj.setNewValue(equipHistorySelected.getReferenceId()==null?"":equipHistorySelected.getReferenceId().toString());
                            if(oldValue!=null){
                                obj.setOldValue(oldValue.getReferenceId() == null ? "": oldValue.getReferenceId().toString());
                            }else {
                                obj.setOldValue("");
                            }

                            break;
                        case "SPECIFICATION":
                            obj.setFeildName(InventoryConstanst.ACTION_AU_DETAIL.SPECIFICATION);
                            obj.setNewValue(equipHistorySelected.getSpecification()==null? "": equipHistorySelected.getSpecification());
                            if(detailOld!=null){
                                obj.setOldValue(detailOld.getEquipmentSpecification() == null ? "": detailOld.getEquipmentSpecification());
                            }else {
                                obj.setOldValue("");
                            }

                            break;
                        case "MAINTANCE_PERIOD":
                            obj.setFeildName(InventoryConstanst.ACTION_AU_DETAIL.MAINTANCE_PERIOD);
                            obj.setNewValue(equipHistorySelected.getMaintancePerio()==null ? "":equipHistorySelected.getMaintancePerio().toString());
                            if(detailOld!=null){
                                obj.setOldValue(detailOld.getMaintancePeriod()==null ? "":detailOld.getMaintancePeriod().toString());
                            }else {
                                obj.setOldValue("");
                            }
                            break;
                        case "LIFE_CYCLE":
                            obj.setFeildName(InventoryConstanst.ACTION_AU_DETAIL.LIFE_CYCLE);
                            obj.setNewValue(equipHistorySelected.getLifeCycle()==null?"":equipHistorySelected.getLifeCycle().toString());
                            if(detailOld!=null){
                                obj.setOldValue(detailOld.getLifeCycle()==null? "":detailOld.getLifeCycle().toString());
                            }else {
                                obj.setOldValue("");
                            }

                            break;
                        case "NOTE":
                            obj.setFeildName(InventoryConstanst.ACTION_AU_DETAIL.NOTE);
                            obj.setNewValue(equipHistorySelected.getNote()+"");
                            if(oldValue!=null){
                                obj.setOldValue(oldValue.getNote() == null? "": oldValue.getNote());
                            }else {
                                obj.setOldValue("");
                            }
                            break;
                    }
                }catch (Exception ex2){
                    ex2.printStackTrace();
                }
                lstHistoryDetail.add(obj);
            }
            equipmentHistoryDetailService.saveOrUpdate(lstHistoryDetail);

        }catch (Exception ex){
            throw  ex;
        }
    }
    
    public void action() {
        FacesMessage message = null;
        try {
        for (EquipmentsDetail detail: lstEquipmentsDetailSelected){
            EquipmentsProfile profile = equipmentsProfileService.findByProfileId(detail.getEquipmentsProfileId());
            equipmentsDetailSelected = detail;
            for (EquipmentHistory his : detail.getEquipmentHistories()){
                equipHistorySelected = his;
                if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    StockGoodsInvSerial sgis  = businessService.findBySerial(detail.getSerial());
                    //todo: lay stock cu o day
                    if(detail.getStockStatusId().toString().equals(InventoryConstanst.StockStatus.USED)
                            && his.getEquipmentStatus().equals(InventoryConstanst.GoodsStatus.ERROR)){
                        sgis.setStockStatus(InventoryConstanst.StockStatus.EX_USED);
                    }else if(!detail.getStockStatusId().toString().equals(InventoryConstanst.StockStatus.INSTOCK)){
                        sgis.setStockStatus(InventoryConstanst.StockStatus.USED);
                    }
                    sgis.setEquipmentProfileStatus(his.getEquipmentStatus());
                    EquipmentsDetail equipmentsDetailUpdate = equipmentsDetailService.findById(detail.getId());
                    equipmentsDetailUpdate.setLastUsedDate(DateTimeUtils.getDate());
                    equipmentsDetailUpdate.setStatus(his.getEquipmentStatus());
                    equipmentsDetailSelected.setLastUsedDate(DateTimeUtils.getDate());
//                    equipmentsDetailSelected.setEquipmentStatus(his.getEquipmentStatus());
                    equipmentsDetailService.saveOrUpdate(equipmentsDetailUpdate);

                    businessService.saveOrUpdateSerial(sgis);
                    if(detail.getStockStatusId().toString().equals(InventoryConstanst.StockStatus.USED)
                            || detail.getStockStatusId().toString().equals(InventoryConstanst.StockStatus.EX_USED)){
                        //khong cap nhan so luong vao stockgood voi thiet bi da xuat ktv, da lap dat
                    }else{
                        if(!detail.getStatus().equals(his.getEquipmentStatus())){
                            businessService.updateQuantity(true, 1L, profile.getProfileId(), detail.getStatus(), detail.getShopId());
                            StockGoods stockGoods = businessService.getStockGoodQuantity(profile.getProfileId(), his.getEquipmentStatus(),
                                    detail.getShopId());
                            if (stockGoods == null){
                                stockGoods = new StockGoods();
                                stockGoods.setShopId(detail.getShopId());
                                stockGoods.setGoodsId(profile.getProfileId());
                                stockGoods.setQuantity(1L);
                                stockGoods.setAvailableQuantity(1L);
                                stockGoods.setGoodsStatus(his.getEquipmentStatus());
                                businessService.saveOrUpdateStockGoods(stockGoods);
                            }else {
                                businessService.updateQuantity(false, 1L, profile.getProfileId(), his.getEquipmentStatus(), detail.getShopId());
                            }
                        }
                    }
                }else{
                    //khong chay vao ham nay
                    StockGoodsInvNoSerial sgins = businessService.getStockGoodsNoserial(detail.getEquipmentsProfileId(), detail.getStatus(),
                            detail.getShopId(), detail.getStockStatusId().toString());
                    if(sgins!=null){
                        if(detail.getStockStatusId().toString().equals(InventoryConstanst.StockStatus.USED)
                                && his.getEquipmentStatus().equals(InventoryConstanst.GoodsStatus.ERROR)){
                            sgins.setStockStatus(InventoryConstanst.StockStatus.INSTOCK);
                        }else if(!detail.getStockStatusId().toString().equals(InventoryConstanst.StockStatus.INSTOCK)){
                            sgins.setStockStatus(InventoryConstanst.StockStatus.USED);
                        }
                        sgins.setEquipmentStatus(his.getEquipmentStatus());
                        EquipmentsDetail equipmentsDetailUpdate = equipmentsDetailService.findById(detail.getId());
                        equipmentsDetailUpdate.setLastUsedDate(DateTimeUtils.getDate());
                        equipmentsDetailUpdate.setStatus(his.getEquipmentStatus());
                        equipmentsDetailSelected.setLastUsedDate(DateTimeUtils.getDate());
//                        equipmentsDetailSelected.setEquipmentStatus(his.getEquipmentStatus());
                        equipmentsDetailService.saveOrUpdate(equipmentsDetailUpdate);
                        businessService.saveOrUpdateNoSerial(sgins);
                    }
                }
                if (his.getEquipmentHistoryId()==null){
                    insertHistory();
                }
            }
        }
        lstEquipmentsDetailSelected.clear();
        equipmentsDetailShow= new EquipmentsDetail();
            equipmentsDetailSelected = null;
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
                    languageBean.getMessage("common.action.save.success", null, false));
            lsData = new LazyEquipmentsModel(fromSerial, getToSerial(), equipmentsDetail, equipmentsGroupId, listShop);
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("frm:dtSearchEtagSerial");
        } catch (Exception ex) {
            ex.printStackTrace();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getMessage("common.confirm.header", null, false),
                    languageBean.getMessage("common.action.save.error", null, false));
        }
        RequestContext.getCurrentInstance().showMessageInDialog(message);

    }

    public void resetCancel(){
        lstEquipmentsDetailSelected.clear();
        equipmentsDetailShow= new EquipmentsDetail();
        equipmentsDetailSelected = null;
        reset();
    }
    public void pageClick(PageEvent event) {
        int var = event.getPage();
//        if(lsData.getRowCount()>0){
//            lsData.setRowIndex(0);
//        }
    }

    public void pageClickDetail(PageEvent event) {
        int var = event.getPage();
//        if(lsData.getRowIndex() < 0){
//            lsData.setRowIndex(0);
//        }
    }

    /**
     * @return the sessionBean
     */
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    /**
     * @param sessionBean the sessionBean to set
     */
    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    /**
     * @return the languageBean
     */
    public LanguageBean getLanguageBean() {
        return languageBean;
    }

    /**
     * @param languageBean the languageBean to set
     */
    public void setLanguageBean(LanguageBean languageBean) {
        this.languageBean = languageBean;
    }

    /**
     * @return the listShop
     */
    public List<Shop> getListShop() {
        return listShop;
    }

    /**
     * @param listShop the listShop to set
     */
    public void setListShop(List<Shop> listShop) {
        this.listShop = listShop;
    }

    /**
     * @return the shopId
     */
    public Long getShopId() {
        return shopId;
    }

    /**
     * @param shopId the shopId to set
     */
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    /**
     * @return the listEquipments
     */
    public List<EquipmentsProfile> getListEquipments() {
        return listEquipments;
    }

    /**
     * @param listEquipments the listEquipments to set
     */
    public void setListEquipments(List<EquipmentsProfile> listEquipments) {
        this.listEquipments = listEquipments;
    }

    /**
     * @return the equipmentsId
     */
    public Long getEquipmentsId() {
        return equipmentsId;
    }

    /**
     * @param equipmentsId the equipmentsId to set
     */
    public void setEquipmentsId(Long equipmentsId) {
        this.equipmentsId = equipmentsId;
    }

    /**
     * @return the equipmentsStatusId
     */
    public Long getEquipmentsStatusId() {
        return equipmentsStatusId;
    }

    /**
     * @param equipmentsStatusId the equipmentsStatusId to set
     */
    public void setEquipmentsStatusId(Long equipmentsStatusId) {
        this.equipmentsStatusId = equipmentsStatusId;
    }

    /**
     * @return the listStockStatus
     */
    public List<ApDomain> getListStockStatus() {
        return listStockStatus;
    }

    public List<ApDomain> getLstStockUpdateDetail(){
        List<com.ftu.staff.bo.ApDomain> lstA = new ArrayList<>();
        for (com.ftu.staff.bo.ApDomain ap: listStockStatus) {
            if(ap.getValue().toString().equals(InventoryConstanst.StockStatus.INSTOCK)
                    || ap.getValue().toString().equals(InventoryConstanst.StockStatus.EX_USED)
                    || ap.getValue().toString().equals(InventoryConstanst.StockStatus.USED)){
                lstA.add(ap);
            }
        }
        return lstA;
    }
    public String getStockStatusName(String stockStatusId){
        for (com.ftu.staff.bo.ApDomain ap: listStockStatus) {
            if(ap.getValue().toString().equals(stockStatusId)){
                return ap.getName();
            }
        }
        return "";
    }
    /**
     * @param listStockStatus the listStockStatus to set
     */
    public void setListStockStatus(List<ApDomain> listStockStatus) {
        this.listStockStatus = listStockStatus;
    }

    /**
     * @return the stockStatusId
     */
    public Long getStockStatusId() {
        return stockStatusId;
    }

    /**
     * @param stockStatusId the stockStatusId to set
     */
    public void setStockStatusId(Long stockStatusId) {
        this.stockStatusId = stockStatusId;
    }

    /**
     * @return the fromSerial
     */
    public String getFromSerial() {
        return fromSerial;
    }

    /**
     * @param fromSerial the fromSerial to set
     */
    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    /**
     * @return the lsData
     */
    public LazyDataModel<EquipmentsDetail> getLsData() {
        return lsData;
    }

    /**
     * @param lsData the lsData to set
     */
    public void setLsData(LazyDataModel<EquipmentsDetail> lsData) {
        this.lsData = lsData;
    }

    /**
     * @return the listProvider
     */
    public List<Provider> getListProvider() {
        return listProvider;
    }

    /**
     * @param listProvider the listProvider to set
     */
    public void setListProvider(List<Provider> listProvider) {
        this.listProvider = listProvider;
    }

    /**
     * @return the providerId
     */
    public Long getProviderId() {
        return providerId;
    }

    /**
     * @param providerId the providerId to set
     */
    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    /**
     * @return the etag
     */
    public StockGoodsInvSerial getEtag() {
        return etag;
    }

    /**
     * @param etag the etag to set
     */
    public void setEtag(StockGoodsInvSerial etag) {
        this.etag = etag;
    }

    /**
     * @return the etadDetail
     */
    public EtagDetail getEtadDetail() {
        return etadDetail;
    }

    /**
     * @param etadDetail the etadDetail to set
     */
    public void setEtadDetail(EtagDetail etadDetail) {
        this.etadDetail = etadDetail;
    }

    /**
     * @return the listTrans
     */
    public List<TransactionAction> getListTrans() {
        return listTrans;
    }

    /**
     * @param listTrans the listTrans to set
     */
    public void setListTrans(List<TransactionAction> listTrans) {
        this.listTrans = listTrans;
    }

    /**
     * @return the listStatusTrans
     */
    public List<GoodsStatusTransSerial> getListStatusTrans() {
        return listStatusTrans;
    }

    /**
     * @param listStatusTrans the listStatusTrans to set
     */
    public void setListStatusTrans(List<GoodsStatusTransSerial> listStatusTrans) {
        this.listStatusTrans = listStatusTrans;
    }

    /**
     * @return the getEquipmentsGroupId
     */
    public Long getEquipmentsGroupId() {
        return equipmentsGroupId;
    }

    /**
     * @param equipmentsGroupId the equipmentsGroupId to set
     */
    public void setEquipmentsGroupId(Long equipmentsGroupId) {
        this.equipmentsGroupId = equipmentsGroupId;
    }

    /**
     * @return the equipments
     */
    public String getEquipments() {
        return equipments;
    }

    /**
     * @param equipments the equipments to set
     */
    public void setEquipments(String equipments) {
        this.equipments = equipments;
    }

    /**
     * @return the toSerial
     */
    public String getToSerial() {
        return toSerial;
    }

    /**
     * @param toSerial the toSerial to set
     */
    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    public String getCreateShop() {
        return createShop;
    }

    public void setCreateShop(String createShop) {
        this.createShop = createShop;
    }

    public EquipmentsDetail getEquipmentsDetail() {
        return equipmentsDetail;
    }

    public void setEquipmentsDetail(EquipmentsDetail equipmentsDetail) {
        this.equipmentsDetail = equipmentsDetail;
    }

    public List<Staff> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<Staff> staffs) {
        this.staffs = staffs;
    }

    public EquipmentsDetail getEquipmentsDetailSelected() {
        return equipmentsDetailSelected;
    }

    public void setEquipmentsDetailSelected(EquipmentsDetail equipmentsDetailSelected) {
        this.equipmentsDetailSelected = equipmentsDetailSelected;
    }

    public LazyDataModel<EquipmentHistory> getEquipmentHistories() {
        return equipmentHistories;
    }

    public void setEquipmentHistories(LazyDataModel<EquipmentHistory> equipmentHistories) {
        this.equipmentHistories = equipmentHistories;
    }

    public EquipmentHistory getEquipHistorySelected() {
        return equipHistorySelected;
    }

    public void setEquipHistorySelected(EquipmentHistory equipHistorySelected) {
        if (equipHistorySelected == null) {
            equipHistorySelected = new EquipmentHistory();
        }
        this.equipHistorySelected = equipHistorySelected;
    }

    public EquipmentHistory reset() {
        equipHistorySelected = new EquipmentHistory();
        if (equipmentsDetailSelected == null) return equipHistorySelected;
        setDataHistory();
//        equipmentsDetailShow = new EquipmentsDetail();
//        lstEquipmentsDetailSelected.clear();
        return equipHistorySelected;
    }

    public List<Position> getPositionAll() {
        return positionAll;
    }

    public void setPositionAll(List<Position> positionAll) {
        this.positionAll = positionAll;
    }

    public List<Shop> getLstShop() {
        return lstShop;
    }

    public void setLstShop(List<Shop> lstShop) {
        this.lstShop = lstShop;
    }

    public List<Position> getLstPostion() {
        return lstPostion;
    }

    public void setLstPostion(List<Position> lstPostion) {
        this.lstPostion = lstPostion;
    }

    public List<Shop> getLstShopHis() {
        return lstShopHis;
    }

    public void setLstShopHis(List<Shop> lstShopHis) {
        this.lstShopHis = lstShopHis;
    }

    public List<EquipmentsDetail> getLstEquipmentsDetailSelected() {
        return lstEquipmentsDetailSelected;
    }

    public void setLstEquipmentsDetailSelected(List<EquipmentsDetail> lstEquipmentsDetailSelected) {
        this.lstEquipmentsDetailSelected = lstEquipmentsDetailSelected;
    }

    public EquipmentsDetail getEquipmentsDetailShow() {
        return equipmentsDetailShow;
    }

    public void setEquipmentsDetailShow(EquipmentsDetail equipmentsDetailShow) {
        this.equipmentsDetailShow = equipmentsDetailShow;
    }

    public Boolean getDisableShop() {
        return disableShop;
    }

    public void setDisableShop(Boolean disableShop) {
        this.disableShop = disableShop;
    }
    public void setDisableShopByStatus() {
        if (InventoryConstanst.StockStatus.USED.equals(equipmentsDetailShow.getStockStatusId().toString())
                && equipHistorySelected.getEquipmentStatus().equals(InventoryConstanst.GoodsStatus.ERROR)){
            this.disableShop = true;
            equipHistorySelected.setShopId(null);
            equipHistorySelected.setPositionId(null);
            FacesMessage message = null;
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
                    languageBean.getMessage("position.reset.msg", null, false));
            RequestContext context = RequestContext.getCurrentInstance();
            context.showMessageInDialog(message);
        }else {
            equipHistorySelected.setPositionId(equipmentsDetailShow.getPositionId());
        }

    }

    public Boolean getDisableActionType() {
        return disableActionType;
    }

    public void setDisableActionType(Boolean disableActionType) {
        this.disableActionType = disableActionType;
    }

    public Boolean getDisableGooodStatus() {
        return disableGooodStatus;
    }

    public void setDisableGooodStatus(Boolean disableGooodStatus) {
        this.disableGooodStatus = disableGooodStatus;
    }

    public List<ApDomain> getListTransType() {
        return listTransType;
    }

    public void setListTransType(List<ApDomain> listTransType) {
        this.listTransType = listTransType;
    }

    public List<ApDomain> getListActionType() {
        return listActionType;
    }

    public void setListActionType(List<ApDomain> listActionType) {
        this.listActionType = listActionType;
    }

    public Boolean getDisableBeforeAdd() {
        return disableBeforeAdd;
    }

    public void setDisableBeforeAdd(Boolean disableBeforeAdd) {
        this.disableBeforeAdd = disableBeforeAdd;
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

    private List<Boolean> visibleTable2 = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
    public void onToggleActiveEtag2(ToggleEvent e) {
        visibleTable2.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    public List<Boolean> getVisibleTable2() {
        return visibleTable2;
    }

    public void setVisibleTable2(List<Boolean> visibleTable2) {
        this.visibleTable2 = visibleTable2;
    }

}
