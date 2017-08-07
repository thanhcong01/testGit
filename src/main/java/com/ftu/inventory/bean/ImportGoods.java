package com.ftu.inventory.bean;

import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.bussiness.EquipmentHistoryDetailService;
import com.ftu.inventory.bussiness.EquipmentsDetailService;
import com.ftu.inventory.dao.StockGoodsInvNoSerialDAO;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.java.business.EquipmentsProfileService;
import com.ftu.java.business.ProviderService;
import com.ftu.language.LanguageBean;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.java.bo.SerialA;
import com.ftu.staff.bussiness.AppDomainService;
import com.ftu.utils.AnalysisSerial;
import com.ftu.exportpdf.ExportInventory;
import com.ftu.inventory.dao.ProviderDAO;
import com.ftu.staff.bo.ApDomain;
import com.ftu.utils.DateTimeUtils;
import com.ftu.utils.StringUtils;

import java.io.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ftu.ws.BusinessService;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.Visibility;

/**
 *
 * @author E5420
 */
@ManagedBean(name = "importGoods", eager = true)
@ViewScoped
public class ImportGoods implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orderCode;
    private String usser;
    // private LazyDataModel<Data> lsData;
    private List<StockTransactionDetail> lsG = new ArrayList<>();
    private List<SerialA> lsData = new ArrayList<>();
    private transient StreamedContent path;
    private StockTransactionDetail stockDetail;
    private String fromSerial;
    private String toSerial;
    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;
    @ManagedProperty(value = "#{languageBean}")
    private LanguageBean languageBean;
    @ManagedProperty(value = "#{transactionNotificationBean}")
    private TransactionNotificationBean transactionNotificationBean;
    HashMap<String, StockTransactionSerial> mapSerrial = new HashMap<>();
    private boolean disableAdd;
    private boolean disableUpload;
    private boolean disableUploadNCC = true;
//    private boolean disableBtnKTV;
    private Long providerId;
    private Long goodsId;
    private List<EquipmentsProfile> listGoods;
    private Long goodsGroup;
    private String quantity = "1";
    private transient StreamedContent fileDownload;
    private String temp = "/WEB-INF/template/import_ncc_temp.xlsx";
    private String tempWarranty = "/WEB-INF/template/import_warranty_temp.xlsx";
    private String tempktv = "/WEB-INF/template/import_ktv_temp.xlsx";
    private transient UploadedFile uploadedFile;
    private List<ApDomain> listReason;
    private Long reasonId;
    private String goods;
    private String serialSingle;
    private String SerialFrm;
    private String reasonName;
    private List<Provider> lsProvider;
    private Date actionDate = new Date();
    private String inputType = "0";
    private Long unitType;
    private Long equipType;
    private String shopSingleName;
    private String countCo;
    private String countCq;
    private String waranPrio;
    private Date waranDate;
    private String mainteinPrio;
    private String litecycle;
    private List<ApDomain> lstGroupType;
    private String serialFrom;
    private String providerForm;private boolean f5Refresh = false;
//    private Long equipmentDetailId = null;
    private Long stockTransactionSerialId;
    private String serialTitle="Serial(*)";
    private Boolean disableQuantity = false;
    List<EquipmentsGroup> lsgGroup = new ArrayList<>();
    private EquipmentsProfileService equipmentsProfileService = new EquipmentsProfileService();
    private ProviderService providerService = new ProviderService();
    private EquipmentsDetailService equipmentsDetailService = new EquipmentsDetailService();
    private Long equipmentProfileStatus = 1L;
    private String transactionActionCode;
    private Long transactionActionID;
    private List<TransactionAction> lstTransactionAction = new ArrayList<>();
    private BusinessService businessService = new BusinessService();
    private Date currentDate = new Date();
//    private List<EquipmentsDetail> lstDetail = new ArrayList<>();
    private Long handleType=InventoryConstanst.ImportType.importGood;
    private EquipmentsDetail equipmentsDetailSerial;
    List<EquipmentsProfile> lsg ;
    List<StockGoodsInvSerialDTO> lstSerialStockGoods;
    List<StockGoodsInvSerialDTO> lstSerialStockGoodsAll;
    private String transactionType;
    private String stockStatusSerial = InventoryConstanst.StockStatus.EX_USED;
    Boolean changeWaran = false;
    private String serialNewSingle;
    private String titleSerilNew = "";
    private EquipmentHistoryDetailService equipmentHistoryDetailService = new EquipmentHistoryDetailService();

    @PostConstruct
    public void init() {
    	sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
        lsProvider = sessionBean.getService().getActiveProvider();
        SimpleDateFormat sf = new SimpleDateFormat(".yy.MM.dd.");
        transactionType = InventoryConstanst.TransactionType.IM;
        initOrderCode();
        usser = sessionBean.getStaff().getStaffName();
        if (sessionBean.getLsEquipments() == null || sessionBean.getLsProvider() == null) {
            getLanguageBean().showMeseage("Info", "checkImport");
        }
        changeGoodsGroup();
        AppDomainService service = new AppDomainService();
        listReason = sessionBean.getService().getListReason(InventoryConstanst.TransactionType.IM);
        lstGroupType = service.findAllByType(InventoryConstanst.ApDomainType.EQUIP_TYPE);
        if(lstGroupType==null){
            lstGroupType = new ArrayList<>();
        }

//        lstSerialStockGoods = businessService.getSerialStockGoods(sessionBean.getShop().getShopId());
    }

    public  void initStockStatusSerial(){
        if(handleType.equals(InventoryConstanst.ImportType.importKTV)){
            transactionType = InventoryConstanst.TransactionType.IM_STAFF;
            stockStatusSerial = InventoryConstanst.StockStatus.EX_USED;
        }else {
            if(handleType.equals(InventoryConstanst.ImportType.importGood)){
                stockStatusSerial = InventoryConstanst.StockStatus.INSTOCK;
                transactionType = InventoryConstanst.TransactionType.IM;
            }else {
                stockStatusSerial = InventoryConstanst.StockStatus.EX_WARANTIED;
                transactionType = InventoryConstanst.TransactionType.IM_WARRANTY;
            }
        }
    }
    public void initPXK(){
        if (InventoryConstanst.ImportType.importKTV.equals(handleType)){
            lstTransactionAction = businessService.searchTranByEDtail(InventoryConstanst.TransactionActionCode.EMEX, null);
        }else if(InventoryConstanst.ImportType.importWarranty.equals(handleType)){
            lstTransactionAction = businessService.searchTranByEDtailImportWaran(InventoryConstanst.TransactionActionCode.EX,
                    InventoryConstanst.TransactionType.EX_WARANTY,InventoryConstanst.TransactionType.EX_REPAIR );
        } else{
//            List<TransactionAction> lstAll = businessService.getAllTransactionAction();
            lstSerialStockGoodsAll = businessService.getSerialStockGoodsImportKTV(null, null,
                    null, stockStatusSerial);
            lstTransactionAction = new ArrayList<>();
//            for (TransactionAction obj:lstAll) {
//                for (StockGoodsInvSerialDTO seri : lstSerialStockGoodsAll) {
//                    if(obj.getTransactionActionId().equals(seri.getImTransaction())){
//                        lstTransactionAction.add(obj);
//                        break;
//                    }
//                }
//            }
        }
    }

    public void removeLsg(StockTransactionDetail ss) {
        if (stockDetail!=null&&ss!=null&&stockDetail.getRowkey().equals(ss.getRowkey())){
            lsData.clear();

        }
        List<StockTransactionDetail> lsGrs = new ArrayList<>();
        if (lsG != null) {
            for (StockTransactionDetail ssrs : lsG) {
                if (!(ssrs.getGoodsId().equals(ss.getGoodsId()) && ssrs.getGoodsStatus().equals(ss.getGoodsStatus())
                && (ssrs.getProviderId()!=null && ssrs.getProviderId().equals(ss.getProviderId())))) {
                        lsGrs.add(ssrs);
                }else {
                    for (StockTransactionSerial obj:ssrs.getLsSerial()) {
                        String map = obj.getEquipmentDetail().getSerial() + "-" + obj.getEquipmentDetail().getEquipmentsProfileId().toString() + "-" + obj.getEquipmentDetail().getProviderId().toString();
                        StockTransactionSerial std = mapSerrial.remove(map);
                    }
                }
            }
        }
        lsG = lsGrs;

    }

    public void action() {
        SimpleDateFormat sf = new SimpleDateFormat(".yy.MM.dd.");
        initOrderCode();
        if (lsG.isEmpty()) {
            return;
        }
        if(InventoryConstanst.ImportType.importKTV.equals(handleType)) {
            StockGoodsInvNoSerialDAO sginsDAO = new StockGoodsInvNoSerialDAO();

            for (StockTransactionDetail std: lsG) {
                EquipmentsProfile profile = equipmentsProfileService.findByProfileId(std.getGoodsId());
                if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    StockGoodsInvNoSerial sgins = sginsDAO.getStockGoodsNoserial(std.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL,
                            sessionBean.getShop().getShopId(), stockStatusSerial);
                    if(sgins == null || std.getQuantity() >  sgins.getQuantity()){
                        getLanguageBean().showMeseage("Info", "max.than.ktv", new Object[] {profile.getProfileCode()});
                        return;
                    }
                }
            }
        }
        else if(InventoryConstanst.ImportType.importWarranty.equals(handleType)) {
            StockGoodsInvNoSerialDAO sginsDAO = new StockGoodsInvNoSerialDAO();

            for (StockTransactionDetail std: lsG) {
                EquipmentsProfile profile = equipmentsProfileService.findByProfileId(std.getGoodsId());
                if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    StockGoodsInvNoSerial sgins = sginsDAO.getStockGoodsNoserial(std.getGoodsId(), InventoryConstanst.GoodsStatus.ERROR,
                            sessionBean.getShop().getShopId(), stockStatusSerial);
                    if(sgins == null || std.getQuantity() >  sgins.getQuantity()){
                        getLanguageBean().showMeseage("Info", "max.than.waranty", new Object[] {profile.getProfileName()});
                        return;
                    }
                }
                //su ly truong hop tb da nhap hang bao hanh
                for (StockTransactionSerial s:std.getLsSerial()) {
                    //voi truong hop tb da nhap truoc do la khong thay moi
                    Long sts = s.getEquipmentDetail().getStatusOld();
                    if(InventoryConstanst.GoodsStatus.USED.equals(sts)){
                        getLanguageBean().showMeseage("Info", "max.than.waranty.serial.input", new Object[] {profile.getProfileName(), s.getEquipmentDetail().getSerial() });
                        return;
                    }
                    //voi truong hop da thay moi serial
                    EquipmentHistoryDetail equipmentHistoryDetail = equipmentHistoryDetailService.getDetailBySerialOld(s.getEquipmentDetail().getSerial(),
                            InventoryConstanst.ACTION_AU_DETAIL.SERIAL);
                    if(equipmentHistoryDetail!=null){
                        getLanguageBean().showMeseage("Info", "max.than.waranty.serial.input", new Object[] {profile.getProfileName(), s.getEquipmentDetail().getSerial() });
                        return;
                    }
                }
            }
        }
        try {
            boolean k = sessionBean.getService().importGoods(lsG, sessionBean.getStaff(), orderCode, reasonId, handleType);

            if (k) {
                getLanguageBean().showMeseage("success", "succesProces");
                initOrderCode();
            }
            initPXK();
            reload();
        } catch (Exception ex) {
            ex.printStackTrace();
            getLanguageBean().showMeseage("error", "errorProcess");
        }
    }

    public void initOrderCode(){
        SimpleDateFormat sf = new SimpleDateFormat(".yy.MM.dd.");
        if(InventoryConstanst.ImportType.importGood.equals(handleType)){
            orderCode = InventoryConstanst.PRIFIX_TRANTYPE.IMPORT_NCC + sf.format(new Date()) + sessionBean.getService().getTransactionActionCount(transactionType).toString();
        } else if(InventoryConstanst.ImportType.importKTV.equals(handleType)){
            orderCode = InventoryConstanst.PRIFIX_TRANTYPE.IMPORT_KTV + sf.format(new Date()) + sessionBean.getService().getTransactionActionCount(transactionType).toString();
        }else if(InventoryConstanst.ImportType.importWarranty.equals(handleType)){
            orderCode = InventoryConstanst.PRIFIX_TRANTYPE.IMPORT_WARRAN + sf.format(new Date()) + sessionBean.getService().getTransactionActionCount(transactionType).toString();
        }

    }

    public  void initCbb(){
        equipType = null;
        if(lstGroupType!=null && !lstGroupType.isEmpty()){
            equipType =lstGroupType.get(0).getValue();
        }
        if(lsProvider!=null&&!lsProvider.isEmpty()){
            providerId = lsProvider.get(0).getProviderId();
        }
        selectEquipType();
//        goodsGroup = null;
//        if(lsgGroup!=null && !lsgGroup.isEmpty()){
//            goodsGroup = lsgGroup.get(0).getEquipmentsGroupId();
//        }
//        goods = "";
//        goodsId = 0L;
//        changeGoodsGroup();
        if(goodsId != null && goodsId > 0L){
            goodsSelect();
        }
    }
    public void changeWaranty(){
        if(changeWaran){
            titleSerilNew = " (*)";
            equipmentProfileStatus = InventoryConstanst.GoodsStatus.NOMAL;
        }else {
            titleSerilNew = "";
            serialNewSingle = "";
            equipmentProfileStatus = InventoryConstanst.GoodsStatus.USED;
        }
    }
    public void beforceSave() {
//        FacesMessage message = null;
        RequestContext context = RequestContext.getCurrentInstance();
        resetInput();
        if (inputType.equals("1")){
//            initCbb();
            context.execute("PF('dlg').hide();");
            context.execute("document.getElementById('frm:import_single').click(); ");
        }else {
//            initCbb();
            context.execute("PF('dlgSingle').hide();");
            context.execute("document.getElementById('frm:import').click(); ");
        }
        if(handleType.equals(InventoryConstanst.ImportType.importWarranty)){
            equipmentProfileStatus = InventoryConstanst.GoodsStatus.USED;
            changeWaran = false;
            serialNewSingle="";
        }
        f5Refresh = true;
        inputType = "0";
//        if (equipmentsDetailSelected == null || equipmentsDetailSelected.getId() == null) {
//            message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false),
//                    languageBean.getMessage("common.acction.beforesave", null, false));
//            RequestContext context = RequestContext.getCurrentInstance();
//            context.showMessageInDialog(message);
//        } else {
//            RequestContext context = RequestContext.getCurrentInstance();
//            context.execute("document.getElementById('frm:beforeAdd').click(); ");
//        }
    }

    public void showConfirm(){
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("document.getElementById('frm:confirmButton').click(); ");
//        context.execute("PF('confirmButton').click();");
    }
    public void beforSave(){
        if (lsG.isEmpty()) {
            getLanguageBean().showMeseage("Info", "import.not.equip");
            return;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('cfImNCC').show();");
    }
    public void save() {

        if (lsG.isEmpty()) {
            return;
        }
        action();
        
        
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('cfImNCC').hide();");
//        reasonName = getReasonName(reasonId);
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.execute("PF('cfImport').show();");
    }

    private String getReasonName(Long reasonId) {
        if (listReason != null) {
            for (ApDomain d : listReason) {
                if (d.getValue().equals(reasonId)) {
                    return d.getName();
                }
            }
        }
        return "";
    }

    public void changeGoodsGroup() {
        if (lsgGroup != null && !lsgGroup.isEmpty() && (goodsGroup == null || goodsGroup == 0L)) {
            goodsGroup = lsgGroup.get(0).getEquipmentsGroupId();
        }
        stockTransactionSerialId = null;
        serialSingle = "";
        listGoods = new ArrayList<>();
        if (goodsGroup != null && goodsGroup > 0) {
            if (lsg != null && !lsg.isEmpty()) {
                for (EquipmentsProfile g : lsg) {
                    if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
                        continue;
                    }
                    if(InventoryConstanst.ImportType.importWarranty.equals(handleType)){
                        if (goodsGroup.equals(g.getEquipmentsGroupId())
                                && InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(g.getManagementType())) {
                            listGoods.add(g);
                        }
                    }else {
                        if (goodsGroup.equals(g.getEquipmentsGroupId())) {
                            listGoods.add(g);
                        }
                    }

                }
            }
        }
        else {
        	goodsId = 0L;
            goods = "";
        	return;	
        }
        
        if (!listGoods.isEmpty()) {
            EquipmentsProfile g = listGoods.get(0);
            goodsId = g.getProfileId();
            goods = g.getProfileCode() + "-" + g.getProfileName();
            lstSerialStockGoods = businessService.getSerialStockGoodsImportKTV(sessionBean.getShop().getShopId(), goodsId,
                    null, stockStatusSerial);
            stockTransactionSerialId = null;
            changEquipmentDetailSerial();
        } else {
            goodsId = 0L;
            goods = "";
        }
        initInputSerial();
        if(goodsId != null && goodsId > 0L){
            goodsSelect();
        }
    }

    public void initInputSerial(){
        serialTitle = "Serial";
        disableQuantity = false;
        if(goodsId!=null&&goodsId>0&&lsg != null){
            for (EquipmentsProfile g : lsg) {
                if(g.getProfileId().equals(goodsId)){
                    if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(g.getManagementType())){
                        disableQuantity = true;
                        serialTitle = "Serial(*)";
                        quantity = "1";
                    }
                    return;
                }
            }
        }

    }

    public void goodsSelect() {
        if(goods==null || goods.isEmpty()){
            if(goodsId!=null && goodsId > 0L){
                EquipmentsProfile profile = equipmentsProfileService.findByProfileId(goodsId);
                goods = profile.getProfileCode() + "-" + profile.getProfileName();
            }
            return;
        }
        boolean dk = true;
        if (listGoods != null) {
            if(!InventoryConstanst.ImportType.importGood.equals(handleType)){
                resetNotSerial();
            }
            for (EquipmentsProfile g : listGoods) {
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
                    continue;
                }
                if ((g.getProfileCode() + "-" + g.getProfileName()).equals(goods)) {
                    goodsId = g.getProfileId();
                    dk = false;
                    if (goodsId!=null&&goodsId>0)
                    {
//                        lstDetail =  equipmentsDetailService.findByProfile(goodsId);
                        lstSerialStockGoods = businessService.getSerialStockGoodsImportKTV(sessionBean.getShop().getShopId(), goodsId,
                                null, stockStatusSerial);
//                        serialFrom="";
//                        equipmentDetailId = null;
                        stockTransactionSerialId = null;
                        equipmentsDetailSerial = null;
                        serialSingle = "";
                        changEquipmentDetailSerial();
                    }
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(g.getManagementType())){
                        disableQuantity = false;
                        resetNotSerial();
                    }
                    break;
                }
            }
            if(dk){
                goodsId =null;
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("frmDlg:uploadBtnSingle");
        if (!handleType.equals(InventoryConstanst.ImportType.importGood)){
            changEquipmentDetailSerial();
        }
        initInputSerial();
    }

    public void providerSelect() {
        if(providerForm==null || providerForm.isEmpty()){
            providerId = null;
            return;
        }
        boolean dk = true;
        if (lsProvider != null) {
            for (Provider g : lsProvider) {
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.toString().equals(g.getStatus())){
                    continue;
                }
                if ((g.getProviderCode() + "-" + g.getProviderName()).equals(providerForm)) {
                    providerId = g.getProviderId();
                    selectProvider();
                    break;
                }
            }
        }
    }

//    public void changSerial() {
////        equipmentDetailId = null;
//        if (lstDetail != null) {
//            for (EquipmentsDetail g : lstDetail) {
//                if ((g.getId()).equals(equipmentDetailId)) {
//                    serialFrom = g.getSerial();
//                    providerId = g.getProviderId();
//                    break;
//                }
//            }
//        }
//    }
    public void changEquipmentDetailSerial() {
//        equipmentDetailId = null;

            EquipmentsProfile profile = null;
            if(goodsId!=null&&goodsId>0){
                profile = equipmentsProfileService.findByProfileId(goodsId);
            }
            StockGoodsInvSerialDTO selected  = null;
            if(stockTransactionSerialId!=null){
                for (StockGoodsInvSerialDTO obj:lstSerialStockGoods) {
                    if(obj.getId().equals(stockTransactionSerialId)){
                        selected = obj;
                        break;
                    }
                }
            }

            if(selected != null && selected.getEquimentDetailId() != null){
                transactionActionCode = "";
                equipmentsDetailSerial = equipmentsDetailService.findById(selected.getEquimentDetailId());
                equipmentsDetailSerial.setStatusOld(equipmentsDetailSerial.getStatus());
                if(InventoryConstanst.ImportType.importWarranty.equals(handleType)){
                    transactionActionID = selected.getCurrentTaId();
                    transactionActionCode = "";
                    for (int i = 0; i < lstTransactionAction.size(); i++) {
                        TransactionAction skin = lstTransactionAction.get(i);
                        if (skin.getTransactionActionId().equals(transactionActionID)) {
                            transactionActionCode = skin.getTransactionActionCode();
                            break;
                        }
                    }
                }
            }
            if(profile!=null){
                if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    if(equipmentsDetailSerial!=null){
                        serialFrom = equipmentsDetailSerial.getSerial();
                        providerId = equipmentsDetailSerial.getProviderId();
                        countCo = equipmentsDetailSerial.getCoNumber() == null ? "": equipmentsDetailSerial.getCoNumber();
                        countCq = equipmentsDetailSerial.getCqNumber() == null ? "": equipmentsDetailSerial.getCqNumber();
                        waranPrio = equipmentsDetailSerial.getWarrantyPeriod() == null ? "": equipmentsDetailSerial.getWarrantyPeriod().toString();
                        waranDate = equipmentsDetailSerial.getWarantyExpiredDate();
                        mainteinPrio = equipmentsDetailSerial.getMaintancePeriod() == null ? "": equipmentsDetailSerial.getMaintancePeriod().toString();
                        litecycle = equipmentsDetailSerial.getLifeCycle() == null ? "": equipmentsDetailSerial.getLifeCycle().toString();;

                        if(handleType.equals(InventoryConstanst.ImportType.importKTV)){
                            equipmentProfileStatus = equipmentsDetailSerial.getStatus();
                        }
                    }
                }else{
                    List<EquipmentsDetail> eDetail = equipmentsDetailService.findByProfile(profile.getProfileId());
                    if(eDetail!=null && !eDetail.isEmpty()){
                        EquipmentsDetail obj = eDetail.get(0);
                        equipmentsDetailSerial = obj;
                        equipmentsDetailSerial.setStatusOld(equipmentsDetailSerial.getStatus());
                        serialFrom = "";
                        providerId = obj.getProviderId();
                        waranPrio = obj.getWarrantyPeriod() == null ? "": obj.getWarrantyPeriod().toString();
                        waranDate = obj.getWarantyExpiredDate();
                        mainteinPrio = obj.getMaintancePeriod() == null ? "": obj.getMaintancePeriod().toString();
                        litecycle = obj.getLifeCycle() == null ? "": obj.getLifeCycle().toString();
                        countCo = obj.getCoNumber() == null ? "": obj.getCoNumber();;
                        countCq = obj.getCqNumber() == null ? "": obj.getCqNumber();;
                        if(handleType.equals(InventoryConstanst.ImportType.importKTV)){
                            equipmentProfileStatus = equipmentsDetailSerial.getStatus();
                        }
                    }
                }
                if (handleType.equals(InventoryConstanst.ImportType.importWarranty)){
                    RequestContext context = RequestContext.getCurrentInstance();
//                    context.update("frmDlg:quantitySingle");
                    context.update("frmDlg:equipmentProfileStatus");
                    context.update("frmDlg:waranPrioSig");
                    context.update("frmDlg:waranDate");
                    context.update("frmDlg:mainteinPrioSig");
                    context.update("frmDlg:litecycleSig");
                }
            }
    }



    public List<String> completeGoods(String gs) {
        goods = gs;
        List<String> rs = new ArrayList<>();
        if (listGoods != null && !listGoods.isEmpty()) {
            for (EquipmentsProfile g : listGoods) {
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
                    continue;
                }
                if (gs == null || gs.isEmpty() || (g.getProfileCode() + "-" + g.getProfileName()).toLowerCase().contains(goods.toLowerCase())) {
                    rs.add(g.getProfileCode() + "-" + g.getProfileName());
                }
            }
        }
        goodsSelect();
        return rs;
    }

    public List<String> completeProvider(String gs) {
        providerForm = gs;
        List<String> rs = new ArrayList<>();
        if (lsProvider != null && !lsProvider.isEmpty()) {
            for (Provider p : lsProvider) {
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.toString().equals(p.getStatus())){
                    continue;
                }
                if (gs == null || gs.isEmpty() || (p.getProviderCode() + "-" + p.getProviderName()).toLowerCase().contains(providerForm.toLowerCase())) {
                    rs.add(p.getProviderCode() + "-" + p.getProviderName());
                }
            }
        }
        return rs;
    }

    public List<String> completeSerial(String gs) {
        serialSingle = gs;
        List<String> rs = new ArrayList<>();
        if (lstSerialStockGoods != null && !lstSerialStockGoods.isEmpty()) {
            for (StockGoodsInvSerialDTO dto : lstSerialStockGoods) {
//                if(InventoryConstanst.ImportType.importKTV.equals(handleType)
//                        && !sessionBean.getStaff().getStaffId().equals(dto.getStaffId())){
//                    continue;
//                }
                if (gs == null || gs.isEmpty() || dto.getSerial().toLowerCase().contains(serialSingle.toLowerCase())) {
                    rs.add(dto.getSerial());
                }
            }
        }
        serialSelect();
        return rs;
    }

    public void serialSelect() {
        stockTransactionSerialId = null;
        if (lstSerialStockGoods != null) {
            for (StockGoodsInvSerialDTO dto : lstSerialStockGoods) {
                if (dto.getSerial().equals(serialSingle)) {
                    stockTransactionSerialId = dto.getId();
                    changEquipmentDetailSerial();
                    break;
                }
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("frmDlg:uploadBtnSingle");
    }

    public void handleSingle(){
        //them moi khong duoc 1 profile duoc 2 nha cung cap. thieu validate du lieu
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            Provider prv = providerService.findById(providerId);
            if(prv==null){
                prv = new Provider();
            }
            if(InventoryConstanst.ProviderStatus.IN_ACTIVE.toString().equals(prv.getStatus())){
                String s = languageBean.getBundle().getString("import.pvd.not.active" );
                s = MessageFormat.format(s, serialFrom);
                languageBean.showMeseage("Info", s);
                context.update("frm:messages");
                return;
            }
            EquipmentsProfile profile = equipmentsProfileService.findByProfileId(goodsId);
            if(profile==null){
                profile = new EquipmentsProfile();
            }
            if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(profile.getEquipProfileStatus())){
                String s = languageBean.getBundle().getString("import.profile.not.active" );
                s = MessageFormat.format(s, serialFrom);
                languageBean.showMeseage("Info", s);
                context.update("frm:messages");
                return;
            }
            String map = serialFrom + "-" + profile.getProfileId().toString() + "-" + prv.getProviderId().toString();
            StockTransactionSerial std = mapSerrial.get(map);

            // Check duplicate serial, provider, good_id in file
            if (serialFrom!=null && !serialFrom.isEmpty() && std != null) {
                String s = languageBean.getBundle().getString("errFile");
                s = MessageFormat.format(s, serialFrom);
                languageBean.showMeseage("Info", s);
                context.update("frm:messages");
                return;
            }
            if(handleType.equals(InventoryConstanst.ImportType.importGood) && equipmentsDetailService.findBySerialAndProviderIdEquip(serialFrom.trim(), prv.getProviderId(), profile.getProfileId())!=null){
                String s = languageBean.getBundle().getString("importErrorSerial.exit");
//                    s = MessageFormat.format(s, i + 1);
                // getLanguageBean().showMeseage_("Info", s);
//                getLanguageBean().showMeseage("error", "errorProcess");
                languageBean.showMeseage("Info", s);
                context.update("frm:messages");
                return;
            }
            Boolean k = false;
//            EquipmentsDetailService equipmentsDetailService = new EquipmentsDetailService();
            for (StockTransactionDetail g : getLsG()) {
                if (goodsId.equals(g.getGoodsId()) && providerId.equals(g.getProviderId()) ) {
                    EquipmentsDetail obj = new EquipmentsDetail();
                    StockTransactionSerial e = new StockTransactionSerial(serialFrom, serialFrom, serialFrom, serialFrom, 1l, goodsId
                            , providerId);
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        List<EquipmentsDetail> eDetail = equipmentsDetailService.findByProfile(profile.getProfileId());
                        if(eDetail!=null && !eDetail.isEmpty()){
                            obj = eDetail.get(0);
                        }
                    }

                    obj.setReferenCode(transactionActionCode);
                    obj.setSerial(serialFrom);
                    obj.setEquipmentsProfileId(goodsId);
                    obj.setProviderId(providerId);
                    obj.setProviderName(prv.getProviderName());
                    obj.setProviderCode(prv.getProviderCode());
                    obj.setStatus(equipmentProfileStatus);
                    obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
//                    obj.setWarrantyCount(0L);
                    obj.setLifeCycle(litecycle.trim().isEmpty()? null:Long.parseLong(litecycle));
                    obj.setMaintancePeriod(mainteinPrio.trim().isEmpty()? null:Long.parseLong(mainteinPrio));
                    obj.setWarantyExpiredDate(waranDate);
                    obj.setWarrantyPeriod(waranPrio.trim().isEmpty()?null:Long.parseLong(waranPrio));
                    obj.setCoNumber(countCo+"");
                    obj.setCqNumber(countCq+"");
                    obj.setProfileName(profile.getProfileName());
                    obj.setEquipmentsProfileCode(profile.getProfileCode());
                    obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
                    obj.setEquipmentSpecification(profile.getSpecification());
//                    obj.setLastUsedDate(new Date());

                    e.setEquipmentDetail(obj);
                    e.setTransactionActionCode(orderCode);
                    e.setProfileManagementType(profile.getManagementType());
                    e.setTransactionActionCode(transactionActionCode);

                    g.setGoodsId(profile.getProfileId());
                    g.setUnitType(profile.getUnit());
                    g.setGoodsStatus(equipmentProfileStatus);
                    g.setProviderId(prv.getProviderId());
                    g.setWanranprio(waranPrio.trim().isEmpty()?null:Long.parseLong(waranPrio));
                    g.setWanranExpriDate(waranDate);
                    g.setCountCo(countCo+"");
                    g.setCountCq(countCq+"");
                    g.setOrdercode(orderCode);
                    g.setProfileManagementType(profile.getManagementType());

                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        if(g.getLsSerial().isEmpty()){
                            g.getLsSerial().add(e);
                        }
                        g.setQuantity(g.getQuantity() + Long.parseLong(quantity.trim()));
                        e.setQuantity(g.getQuantity());
                    }else {
                        g.getLsSerial().add(e);
                        g.setQuantity(Long.parseLong(g.getLsSerial().size()+""));
                        e.setQuantity(1L);
                    }
                    mapSerrial.put(map, e);
                    k = true;
                    break;
                }

            }
            if (!k) {
                StockTransactionDetail g = new StockTransactionDetail(0L,  profile.getProfileId(), 1l);
                g.setOrdercode(orderCode);
                StockTransactionSerial e = new StockTransactionSerial(serialFrom, serialFrom, serialFrom, serialFrom, 1l, goodsId
                        , providerId);
                EquipmentsDetail obj = new EquipmentsDetail();
                if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    List<EquipmentsDetail> eDetail = equipmentsDetailService.findByProfile(profile.getProfileId());
                    if(eDetail!=null && !eDetail.isEmpty()){
                        obj = eDetail.get(0);
                    }
                }
                obj.setReferenCode(transactionActionCode);
                obj.setSerial(serialFrom);
                obj.setEquipmentsProfileId(goodsId);
                obj.setProviderId(providerId);
                obj.setProviderName(prv.getProviderName());
                obj.setProviderCode(prv.getProviderCode());
                obj.setStatus(equipmentProfileStatus);
                obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
//                obj.setWarrantyCount(0L);
                obj.setLifeCycle(litecycle.trim().isEmpty()?null:Long.parseLong(litecycle));
                obj.setMaintancePeriod(mainteinPrio.trim().isEmpty()?null:Long.parseLong(mainteinPrio));
                obj.setWarantyExpiredDate(waranDate);
                obj.setWarrantyPeriod(waranPrio.trim().isEmpty()?null:Long.parseLong(waranPrio));
                obj.setCoNumber(countCo+"");
                obj.setCqNumber(countCq+"");
                obj.setProfileName(profile.getProfileName());
                obj.setEquipmentsProfileCode(profile.getProfileCode());
                obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
                obj.setEquipmentSpecification(profile.getSpecification());
//                obj.setLastUsedDate(new Date());
                e.setEquipmentDetail(obj);
                e.setProfileManagementType(profile.getManagementType());
                e.setTransactionActionCode(orderCode);
//                g.getLsSerial().add(e);
                g.setGoodsId(profile.getProfileId());
                g.setUnitType(profile.getUnit());
                g.setGoodsStatus(equipmentProfileStatus);
                g.setProviderId(prv.getProviderId());
                g.setWanranprio(waranPrio.trim().isEmpty()?null:Long.parseLong(waranPrio));
                g.setWanranExpriDate(waranDate);
                g.setCountCo(countCo+"");
                g.setCountCq(countCq+"");
                g.setOrdercode(orderCode);
                g.setReferenCode(transactionActionCode);
                g.setProfileManagementType(profile.getManagementType());
                if(quantity==null||quantity.isEmpty()){
                    g.setQuantity(1L);
                }else {
                    g.setQuantity(Long.parseLong(quantity.trim()));
                }
                if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    if(g.getLsSerial().isEmpty()){
                        g.getLsSerial().add(e);
                    }
                    g.setQuantity(Long.parseLong(quantity.trim()));
                    e.setQuantity(g.getQuantity());
                }else {
                    g.getLsSerial().add(e);
                    g.setQuantity(Long.parseLong(g.getLsSerial().size()+""));
                    e.setQuantity(1L);

                }
//                e.setQuantity(g.getQuantity());
                mapSerrial.put(map, e);
                getLsG().add(g);

            }
            viewClick();
            context.execute("PF('dlgSingle').hide();");
        }catch (Exception ex){
            ex.printStackTrace();
        }
        f5Refresh = false;
    }


    public void handleFileUpload() throws IOException {
        if(!f5Refresh) return;
        RequestContext context = RequestContext.getCurrentInstance();

        List<StockTransactionDetail> lsRt = new ArrayList<>();
        HashMap<String, StockTransactionSerial> mapSerrialBk = new HashMap<>();
        mapSerrialBk.putAll(mapSerrial);
        if (lsG != null) {
//            lsRt.addAll(lsG);
            for (StockTransactionDetail stds : lsG) {
                lsRt.add(new StockTransactionDetail(stds));
            }
        }
        if (uploadedFile == null) {
            languageBean.showMeseage("Info", "emtyFile");
            context.update("frm:messages");
            return;
        }
        if (!uploadedFile.getFileName().endsWith(".xlsx")) {
            languageBean.showMeseage("Info", "errFileType");
            context.update("frm:messages");
            return;
        }
//        int qtt = 0;
//        if (providerId==null || goodsId==null || providerId < 0 || goodsId < 0 || quantity.trim().isEmpty()) {
//            languageBean.showMeseage("Info", "importReq");
//            context.update("frm:messages");
//            return;
//        }
//        if (quantity.matches("[\\d]+$")) {
//            qtt = new Integer(quantity);
//        } else {
//            languageBean.showMeseage("Info", "quantityReqErr");
//            context.update("frm:messages");
//            return;
//        }
        try {
            InputStream file = uploadedFile.getInputstream();
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getLastRowNum();
            if (rowsCount < 1) {
                languageBean.showMeseage("Info", "import.not.empty");
                return;
            }
            Boolean error = false;
            Boolean errorCheck = false;
//            Provider prv = sessionBean.getProvider(providerId.toString());
//            sheet.getRow(0).getCell(4).setCellType(XSSFCell.CELL_TYPE_STRING);
//            sheet.getRow(0).getCell(4).setCellValue("Kết quả kiểm tra");
            DataFormatter df = new DataFormatter();
//            EquipmentsProfile profile = equipmentsProfileService.findByProfileId(goodsId);
            Long totalQuantity = 0L;
            int endCel = 10;

            for (int i = 1; i <= rowsCount; i++) {
                Row row = sheet.getRow(i);
                Boolean k = false;
                //NCC
                int column = 0;
                Provider prv = null;
                EquipmentsProfile profile = null;
                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                String providerCode = df.formatCellValue(row.getCell(column)).trim();
                if (providerCode != null && !providerCode.isEmpty()) {
                    prv = providerService.findByProviderCode(providerCode);
                    if(prv==null){
                        String s = languageBean.getBundle().getString("import.ncc.not.exit" );
                        s = MessageFormat.format(s, providerCode);
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                        continue;
                    }
                    providerId = prv.getProviderId();

                }else {
                    String s = languageBean.getBundle().getString("import.ncc.not.input" );
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.toString().equals(prv.getStatus())){
                    String s = languageBean.getBundle().getString("import.pvd.not.active" );
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }
                column++;
                //Ma TB
                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                String profileCode = df.formatCellValue(row.getCell(column)).trim();
                if (profileCode != null && !profileCode.isEmpty()) {
                    profile = equipmentsProfileService.checkExists(profileCode);
                    if(profile==null){
                        String s = languageBean.getBundle().getString("import.profileCode.not.exit" );
                        s = MessageFormat.format(s, profileCode);
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                        continue;
                    }else if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(profile.getEquipProfileStatus())){
                        String s = languageBean.getBundle().getString("profilecode.inactive.not.action" );
                        s = MessageFormat.format(s, profileCode);
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                        continue;
                    }
                    goodsId = profile.getProfileId();
                }else {
                    String s = languageBean.getBundle().getString("import.profile.not.input" );
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(profile.getEquipProfileStatus())){
                    String s = languageBean.getBundle().getString("import.profile.not.active" );
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }
                column++;
                // Serial
                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_STRING);
                String serial = df.formatCellValue(row.getCell(column)).trim();
//                 check empty serial
                if ((serial == null || serial.isEmpty())&& InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())) {
                    String s = languageBean.getBundle().getString("importErrorSerial.empty");
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;

                }else if(!serial.isEmpty() && !InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    String s = languageBean.getBundle().getString("importErrorSerial.only.empty");
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }else if(!serial.trim().isEmpty() && equipmentsDetailService.findBySerial(serial.trim())!=null){
                    String s = languageBean.getBundle().getString("importErrorSerial.exit.only");
//                    s = MessageFormat.format(s, i + 1);
                    // getLanguageBean().showMeseage_("Info", s);
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }else if(!serial.trim().isEmpty() ) {
                    boolean dk = false;
                    for (StockTransactionDetail g : getLsG()) {
                        if(!dk) {
                            for (StockTransactionSerial serialE : g.getLsSerial()) {
                                if (serial.equals(serialE.getSerial())) {
                                    String s = "";
                                    if (InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())) {
                                        s = languageBean.getBundle().getString("importErrorSerial.exit.input");
                                    } else {
                                        s = languageBean.getBundle().getString("importErrorSerial.noserial.exit.input");
                                    }
                                    error = true;
                                    row.createCell(endCel);
                                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                                    row.getCell(endCel).setCellValue(s);
                                    dk = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (dk) continue;
                }

                if(serial.trim().isEmpty()){
                }else if(serial.trim().contains(" ")) {
                    String s = "";
                    s = languageBean.getBundle().getString("importErrorSerial.serial.not.space");
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }
                column++;
                // Thời gian BH
                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                Long warranty_time = null;
                String TgBh = df.formatCellValue(row.getCell(column));
                if (TgBh != null && !TgBh.isEmpty() && (!StringUtils.isNumberLong̣̣(TgBh.trim()) || Long.parseLong(TgBh.trim()) < 0 )) {
                    String s = languageBean.getBundle().getString("importWaranPrio");
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        s = languageBean.getBundle().getString("importWaranPrio.not.serial");
                    }
//                    s = MessageFormat.format(s, i + 1);
                    // getLanguageBean().showMeseage_("Info", s);
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }else if(TgBh != null && !TgBh.isEmpty()) {
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        String s = languageBean.getBundle().getString("importWaranPrio.not.serial");
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                        continue;
                    }
                    warranty_time = Long.parseLong(TgBh.trim());
                }
                column++;
                // Ngày hết hạn BH
                String warranty_expired_date_str = df.formatCellValue(row.getCell(column)).trim();
                Date warranty_expired_date = null;
                if(warranty_expired_date_str != null && !warranty_expired_date_str.isEmpty() && !DateTimeUtils.isValidFormat(warranty_expired_date_str)){
                    String s = languageBean.getBundle().getString("expriceDate.notfomat");
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        s = languageBean.getBundle().getString("expriceDate.notfomat.not.serial");
                    }
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }else if(warranty_expired_date_str!=null && !warranty_expired_date_str.isEmpty()){
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        String s = languageBean.getBundle().getString("expriceDate.notfomat.not.serial");
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                        continue;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    warranty_expired_date = sdf.parse(warranty_expired_date_str);
//                    warranty_expired_date= row.getCell(2).getDateCellValue();
                    if (warranty_expired_date.compareTo(new Date())<=0){
                        String s = languageBean.getBundle().getString("expriceDate.than.today");
//                    s = MessageFormat.format(s, i + 1);
                        // getLanguageBean().showMeseage_("Info", s);
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                    }
                }
                column++;
                // Số CO
                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_STRING);
                String co = df.formatCellValue(row.getCell(column)).trim();
                if (co != null && !co.isEmpty() && co.length()> 30) {
                    String s = languageBean.getBundle().getString("CO.max.length");
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        s = languageBean.getBundle().getString("co.notfomat.not.serial");
                    }
                    s = MessageFormat.format(s, i + 1);
                    // getLanguageBean().showMeseage_("Info", s);
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }else if(co != null && !co.isEmpty() ){
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        String s = languageBean.getBundle().getString("co.notfomat.not.serial");
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                        continue;
                    }
                }
                column++;
                // Số CQ
                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_STRING);
                String cq = df.formatCellValue(row.getCell(column)).trim();
                if (cq != null && !cq.isEmpty() && cq.length() > 20) {
                    String s = languageBean.getBundle().getString("CQ.max.length");
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        s = languageBean.getBundle().getString("cq.notfomat.not.serial");
                    }
//                    s = MessageFormat.format(s, i + 1);
                    // getLanguageBean().showMeseage_("Info", s);
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }else if(cq != null && !cq.isEmpty() ){
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        String s = languageBean.getBundle().getString("cq.notfomat.not.serial");
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                        continue;
                    }
                }
                column++;
                // Chu kỳ bảo trì (tháng)
                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_STRING);
                String maintance_period = df.formatCellValue(row.getCell(column));
                Long maintance_periodL = null;
                if (maintance_period != null && !maintance_period.isEmpty() && (!StringUtils.isNumberLong̣̣(maintance_period.trim()) || Long.parseLong(maintance_period.trim()) < 0 )) {
                    String s = languageBean.getBundle().getString("maintance_period.notfomat");
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        s = languageBean.getBundle().getString("maintance_period.notfomat.not.serial");
                    }
                    s = MessageFormat.format(s, i + 1);
                    // getLanguageBean().showMeseage_("Info", s);
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }else if(maintance_period != null && !maintance_period.isEmpty()) {
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        String s = languageBean.getBundle().getString("maintance_period.notfomat.not.serial");
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                        continue;
                    }
                    maintance_periodL = Long.parseLong(maintance_period) ;
                }
                column++;
                // Vòng đời(tháng)
                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_STRING);
                String life_cycle = df.formatCellValue(row.getCell(column)).trim();
                Long life_cycleL = null;
                if (life_cycle != null && !life_cycle.isEmpty() && (!StringUtils.isNumberLong̣̣(life_cycle.trim()) || Long.parseLong(life_cycle.trim()) < 0 )) {
                    String s = languageBean.getBundle().getString("life_cycle.notfomat");
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        s = languageBean.getBundle().getString("life_cycle.notfomat.not.serial");
                    }
//                    s = MessageFormat.format(s, i + 1);
                    // getLanguageBean().showMeseage_("Info", s);
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }else if(life_cycle != null && !life_cycle.isEmpty()){
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        String s = languageBean.getBundle().getString("life_cycle.notfomat.not.serial");
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                        continue;
                    }
                    life_cycleL = Long.parseLong(life_cycle);
                }
                column++;
                // Số lượng
                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_STRING);
                String quantityIP = df.formatCellValue(row.getCell(column)).trim();
                Long quantityIPL = 1L;
                if (quantityIP != null && !quantityIP.isEmpty() && (!StringUtils.isNumberLong̣̣(quantityIP.trim()) || Long.parseLong(quantityIP.trim()) < 0 )) {
                    String s ="";
                    if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        s = languageBean.getBundle().getString("quantityIP.notfomat.default");
                    }else {
                        s = languageBean.getBundle().getString("quantityIP.notfomat");
                    }
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }else if(quantityIP != null && !quantityIP.isEmpty()){
                    quantityIPL = Long.parseLong(quantityIP);
                    String s ="";
                    if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())
                            && quantityIPL > 1){
                        s = languageBean.getBundle().getString("quantityIP.notfomat.default");
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                        continue;
                    }
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        totalQuantity+=quantityIPL;
                    }

                }
                column++;
                String map = serial + "-" + profile.getProfileId().toString() + "-" + prv.getProviderId().toString();
                StockTransactionSerial std = mapSerrial.get(map);
                if (serial!=null && !serial.isEmpty() &&std != null) {
                    String s = languageBean.getBundle().getString("errFile");
                    s = MessageFormat.format(s, serial);
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }
                if(error){
                    errorCheck = error;
                }
                if(errorCheck){
                    continue;
                }
                for (StockTransactionDetail g : getLsG()) {
                    if (goodsId.equals(g.getGoodsId()) && providerId.equals(g.getProviderId()) ) {
                        EquipmentsDetail obj = new EquipmentsDetail();
                        StockTransactionSerial e = new StockTransactionSerial(serial, serial, serial, serial, 1l, profile.getProfileId()
                                , prv.getProviderId());
                        if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                            List<EquipmentsDetail> eDetail = equipmentsDetailService.findByProfile(profile.getProfileId());
                            if(eDetail!=null && !eDetail.isEmpty()){
                                obj = eDetail.get(0);
                            }
                        }
                        obj.setReferenCode(transactionActionCode);
                        obj.setSerial(serial);
                        obj.setEquipmentsProfileId(profile.getProfileId());
                        obj.setProviderId(prv.getProviderId());
                        obj.setProviderName(prv.getProviderName());
                        obj.setProviderCode(prv.getProviderCode());
                        obj.setStatus(equipmentProfileStatus);
                        obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
                        obj.setLifeCycle(life_cycleL);
                        obj.setMaintancePeriod(maintance_periodL);
                        obj.setWarrantyPeriod(warranty_time);
                        obj.setWarantyExpiredDate(warranty_expired_date);
                        obj.setCoNumber(co);
                        obj.setCqNumber(cq);
                        obj.setProfileName(profile.getProfileName());
                        obj.setEquipmentsProfileCode(profile.getProfileCode());
                        obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
                        obj.setEquipmentSpecification(profile.getSpecification());
//                        obj.setLastUsedDate(new Date());
                        e.setEquipmentDetail(obj);
                        e.setTransactionActionCode(orderCode);
//                        g.getLsSerial().add(e);
                        g.setGoodsId(profile.getProfileId());
                        g.setUnitType(profile.getUnit());
                        g.setGoodsStatus(equipmentProfileStatus);
                        g.setProviderId(prv.getProviderId());
                        g.setWanranprio(warranty_time);
                        g.setMaintancePeriod(maintance_periodL);
                        g.setWanranExpriDate(warranty_expired_date);
                        g.setCountCo(co);
                        g.setCountCq(cq);
                        g.setReferenCode(transactionActionCode);
                        g.setProfileManagementType(profile.getManagementType());
                        if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
//                            g.setQuantity(totalQuantity);
                            if(g.getLsSerial().isEmpty()){
                                g.getLsSerial().add(e);
                            }
                            g.setQuantity(g.getQuantity() + quantityIPL);
                            e.setQuantity(g.getQuantity() + quantityIPL);
                        }else {
                            g.getLsSerial().add(e);
                            g.setQuantity(Long.parseLong(g.getLsSerial().size()+""));
                            e.setQuantity(1L);
                        }
                        mapSerrial.put(map, e);
                        k = true;
                        break;
                    }
                }
                if (!k) {
                    StockTransactionDetail g = new StockTransactionDetail(0L,  profile.getProfileId(), 1l);
                    g.setOrdercode(orderCode);
                    StockTransactionSerial e = new StockTransactionSerial(serial, serial, serial, serial, 1l,  profile.getProfileId(),
                            prv.getProviderId());
                    EquipmentsDetail obj = new EquipmentsDetail();
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        List<EquipmentsDetail> eDetail = equipmentsDetailService.findByProfile(profile.getProfileId());
                        if(eDetail!=null && !eDetail.isEmpty()){
                            obj = eDetail.get(0);
                        }
                    }
                    obj.setReferenCode(transactionActionCode);
                    obj.setSerial(serial);
                    obj.setEquipmentsProfileId(profile.getProfileId());
                    obj.setProviderId(prv.getProviderId());
                    obj.setProviderName(prv.getProviderName());
                    obj.setProviderCode(prv.getProviderCode());
                    obj.setStatus(equipmentProfileStatus);
                    obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
                    obj.setLifeCycle(life_cycleL);
                    obj.setMaintancePeriod(maintance_periodL);
                    obj.setWarantyExpiredDate(warranty_expired_date);
                    obj.setWarrantyPeriod(warranty_time);
                    obj.setCoNumber(co);
                    obj.setCqNumber(cq);
                    obj.setProfileName(profile.getProfileName());
                    obj.setEquipmentsProfileCode(profile.getProfileCode());
                    obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
                    obj.setEquipmentSpecification(profile.getSpecification());
//                    obj.setLastUsedDate(new Date());
                    e.setEquipmentDetail(obj);
                    e.setTransactionActionCode(orderCode);
//                    g.getLsSerial().add(e);
                    g.setGoodsId(profile.getProfileId());
                    g.setUnitType(profile.getUnit());
                    g.setGoodsStatus(equipmentProfileStatus);
                    g.setProviderId(prv.getProviderId());
                    g.setWanranprio(warranty_time);
                    g.setMaintancePeriod(maintance_periodL);
                    g.setWanranExpriDate(warranty_expired_date);
                    g.setCountCo(co);
                    g.setCountCq(cq);
                    g.setReferenCode(transactionActionCode);
                    g.setProfileManagementType(profile.getManagementType());
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        if(g.getLsSerial().isEmpty()){
                            g.getLsSerial().add(e);
                        }
                        g.setQuantity(quantityIPL);
                        e.setQuantity(quantityIPL);

                    }else {
                        g.getLsSerial().add(e);
                        g.setQuantity(Long.parseLong(g.getLsSerial().size()+""));
                        e.setQuantity(1L);
                    }


                    mapSerrial.put(map, e);
                    getLsG().add(g);
                }

            }
            f5Refresh = false;
            if (error) {
                f5Refresh = true;
                lsG.clear();
                lsG.addAll(lsRt);
                mapSerrial.clear();
                mapSerrial.putAll(mapSerrialBk);
//                reload();
                getLanguageBean().showMeseage("Info", "errorReadFile");
                String k = new Long(new Date().getTime()).toString();
                FileOutputStream out = new FileOutputStream(k + ".xlsx");
                workbook.write(out);
                File f = new File(k + ".xlsx");
                downloadFile(f);
                out.flush();
                out.close();

            }
            file.close();
            // context.execute("PF('dlg').show();");
        } catch (Exception ex) {
            ex.printStackTrace();
            lsG = lsRt;
//            reload();
            getLanguageBean().showMeseage("Info", "errFileData");
            uploadedFile.getInputstream().close();
//            f5Refresh = false;
        }

    }

    public void handleSingleKTV(){
        try {
            Provider prv = providerService.findById(providerId);
            if(prv==null){
                prv = new Provider();
            }
            EquipmentsProfile profile = equipmentsProfileService.findByProfileId(goodsId);
            if(profile==null){
                profile = new EquipmentsProfile();
            }

            String map = serialFrom + "-" + profile.getProfileId().toString() + "-" + prv.getProviderId().toString();
            StockTransactionSerial std = mapSerrial.get(map);
            RequestContext context = RequestContext.getCurrentInstance();
            // Check duplicate serial, provider, good_id in file
            if (std != null) {
                String s = languageBean.getBundle().getString("errFile");
                s = MessageFormat.format(s, serialFrom);
                languageBean.showMeseage("Info", s);
                context.update("frm:messages");
                return;
            }
            if(handleType.equals(InventoryConstanst.ImportType.importGood) && equipmentsDetailService.findBySerialAndProviderIdEquip(serialFrom.trim(), prv.getProviderId(), profile.getProfileId())!=null){
                String s = languageBean.getBundle().getString("importErrorSerial.exit");
//                    s = MessageFormat.format(s, i + 1);
                // getLanguageBean().showMeseage_("Info", s);
//                getLanguageBean().showMeseage("error", "errorProcess");
                languageBean.showMeseage("Info", s);
                context.update("frm:messages");
                return;
            }
            lstSerialStockGoodsAll = businessService.getSerialStockGoodsImportKTV(null, null,
                    null, InventoryConstanst.StockStatus.EX_USED);
            Boolean k = false;
//            EquipmentsDetailService equipmentsDetailService = new EquipmentsDetailService();
            for (StockTransactionDetail g : getLsG()) {
                if (goodsId.equals(g.getGoodsId()) && providerId.equals(g.getProviderId()) && g.getGoodsStatus().equals(equipmentProfileStatus) ) {
                    EquipmentsDetail obj = new EquipmentsDetail();
                    if(equipmentsDetailSerial!=null){
                        obj = equipmentsDetailSerial;
                    }

                    StockTransactionSerial e = new StockTransactionSerial(serialFrom, serialFrom, serialFrom, serialFrom, 1l, goodsId
                            , providerId);
                    obj.setReferenCode(transactionActionCode);
                    obj.setSerial(serialFrom);
                    obj.setEquipmentsProfileId(goodsId);
                    obj.setProviderId(providerId);
                    obj.setProviderName(prv.getProviderName());
                    obj.setProviderCode(prv.getProviderCode());
                    obj.setStatus(equipmentProfileStatus);
                    obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
//                    obj.setWarrantyCount(0L);
                    obj.setLifeCycle(litecycle.trim().isEmpty()? null:Long.parseLong(litecycle));
                    obj.setMaintancePeriod(mainteinPrio.trim().isEmpty()? null:Long.parseLong(mainteinPrio));
                    obj.setWarantyExpiredDate(waranDate);
                    obj.setWarrantyPeriod(waranPrio.trim().isEmpty()?null:Long.parseLong(waranPrio));
                    obj.setCoNumber(countCo+"");
                    obj.setCqNumber(countCq+"");
                    obj.setProfileName(profile.getProfileName());
                    obj.setEquipmentsProfileCode(profile.getProfileCode());
                    obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
//                    obj.setLastUsedDate(new Date());

                    e.setEquipmentDetail(obj);
                    e.setTransactionActionCode(orderCode);
                    e.setProfileManagementType(profile.getManagementType());
                    g.getLsSerial().add(e);
                    if(!serialFrom.trim().isEmpty()) {
                        for (StockGoodsInvSerialDTO stockGoodsInvSerialDTO : lstSerialStockGoodsAll) {
                            if (stockGoodsInvSerialDTO.getSerial().equals(serialFrom.trim()) &&
                                    stockGoodsInvSerialDTO.getEquipmentProfileId().equals(goodsId)) {
                                g.getSetSerial().add(stockGoodsInvSerialDTO);
                                break;
                            }
                        }
                    }
                    g.setGoodsId(profile.getProfileId());
                    g.setUnitType(profile.getUnit());
                    g.setGoodsStatus(equipmentProfileStatus);
                    g.setProviderId(prv.getProviderId());
                    g.setWanranprio(waranPrio.trim().isEmpty()?null:Long.parseLong(waranPrio));
                    g.setWanranExpriDate(waranDate);
                    g.setCountCo(countCo+"");
                    g.setCountCq(countCq+"");
                    g.setOrdercode(orderCode);
                    g.setReferenCode(transactionActionCode);
                    g.setProfileManagementType(profile.getManagementType());

                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        g.setQuantity(Long.parseLong(quantity.trim()));
                        e.setQuantity(g.getQuantity());
                    }else {
                        g.setQuantity(Long.parseLong(g.getLsSerial().size()+""));
                        e.setQuantity(1L);
                    }
                    mapSerrial.put(map, e);
                    k = true;
                    break;
                }

            }
            if (!k) {
                StockTransactionDetail g = new StockTransactionDetail(0L,  profile.getProfileId(), 1l);
                g.setOrdercode(orderCode);
                StockTransactionSerial e = new StockTransactionSerial(serialFrom, serialFrom, serialFrom, serialFrom, 1l, goodsId
                        , providerId);
                EquipmentsDetail obj = new EquipmentsDetail();
                if(equipmentsDetailSerial!=null){
                    obj = equipmentsDetailSerial;
                }
                obj.setReferenCode(transactionActionCode);
                obj.setSerial(serialFrom);
                obj.setEquipmentsProfileId(goodsId);
                obj.setProviderId(providerId);
                obj.setProviderName(prv.getProviderName());
                obj.setProviderCode(prv.getProviderCode());
                obj.setStatus(equipmentProfileStatus);
                obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
//                obj.setWarrantyCount(0L);
                obj.setLifeCycle(litecycle.trim().isEmpty()?null:Long.parseLong(litecycle));
                obj.setMaintancePeriod(mainteinPrio.trim().isEmpty()?null:Long.parseLong(mainteinPrio));
                obj.setWarantyExpiredDate(waranDate);
                obj.setWarrantyPeriod(waranPrio.trim().isEmpty()?null:Long.parseLong(waranPrio));
                obj.setCoNumber(countCo+"");
                obj.setCqNumber(countCq+"");
                obj.setProfileName(profile.getProfileName());
                obj.setEquipmentsProfileCode(profile.getProfileCode());
                obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
//                obj.setLastUsedDate(new Date());
                e.setEquipmentDetail(obj);
                e.setProfileManagementType(profile.getManagementType());
                e.setTransactionActionCode(orderCode);
                g.getLsSerial().add(e);
                if(serialFrom != null && !serialFrom.isEmpty()) {
                    for (StockGoodsInvSerialDTO stockGoodsInvSerialDTO : lstSerialStockGoods) {
                        if (stockGoodsInvSerialDTO.getSerial().equals(serialFrom.trim()) &&
                                stockGoodsInvSerialDTO.getEquipmentProfileId().equals(goodsId)) {
                            g.getSetSerial().add(stockGoodsInvSerialDTO);
                            break;
                        }
                    }
                }
                g.setGoodsId(profile.getProfileId());
                g.setUnitType(profile.getUnit());
                g.setGoodsStatus(equipmentProfileStatus);
                g.setProviderId(prv.getProviderId());
                g.setWanranprio(waranPrio.trim().isEmpty()?null:Long.parseLong(waranPrio));
                g.setWanranExpriDate(waranDate);
                g.setCountCo(countCo+"");
                g.setCountCq(countCq+"");
                g.setOrdercode(orderCode);
                g.setReferenCode(transactionActionCode);
                g.setProfileManagementType(profile.getManagementType());
//                if(quantity==null||quantity.isEmpty()){
//                    g.setQuantity(1L);
//                }else {
//                    g.setQuantity(Long.parseLong(quantity.trim()));
//                }
                if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    g.setQuantity(Long.parseLong(quantity.trim()));
                    e.setQuantity(g.getQuantity());
                }else {
                    g.setQuantity(Long.parseLong(g.getLsSerial().size()+""));
                    e.setQuantity(1L);
                }
//                e.setQuantity(g.getQuantity());
                mapSerrial.put(map, e);
                getLsG().add(g);

            }
            viewClick();
            context.execute("PF('dlgSingle').hide();");
        }catch (Exception ex){
            ex.printStackTrace();
        }
        f5Refresh = false;
    }

    public void handleSingleWaranty(){
        try {
            Provider prv = providerService.findById(providerId);
            if(prv==null){
                prv = new Provider();
            }
            EquipmentsProfile profile = equipmentsProfileService.findByProfileId(goodsId);
            if(profile==null){
                profile = new EquipmentsProfile();
            }
            String map = "";
            if(changeWaran){
                map = serialNewSingle + "-" + profile.getProfileId().toString() + "-" + prv.getProviderId().toString();
            }else {
                map = serialFrom + "-" + profile.getProfileId().toString() + "-" + prv.getProviderId().toString();
            }
            StockTransactionSerial std = mapSerrial.get(map);
            RequestContext context = RequestContext.getCurrentInstance();
            // Check duplicate serial, provider, good_id in file
            if (std != null) {
                String s = languageBean.getBundle().getString("errFile");
                s = MessageFormat.format(s, serialFrom);
                languageBean.showMeseage("Info", s);
                context.update("frm:messages");
                return;
            }
            if(handleType.equals(InventoryConstanst.ImportType.importGood) && equipmentsDetailService.findBySerialAndProviderIdEquip(serialFrom.trim(), prv.getProviderId(), profile.getProfileId())!=null){
                String s = languageBean.getBundle().getString("importErrorSerial.exit");
//                    s = MessageFormat.format(s, i + 1);
                // getLanguageBean().showMeseage_("Info", s);
//                getLanguageBean().showMeseage("error", "errorProcess");
                languageBean.showMeseage("Info", s);
                context.update("frm:messages");
                return;
            }
            Boolean k = false;
//            EquipmentsDetailService equipmentsDetailService = new EquipmentsDetailService();
            for (StockTransactionDetail g : getLsG()) {
                if (goodsId.equals(g.getGoodsId()) //&& providerId.equals(g.getProviderId())
                        && g.getGoodsStatus().equals(equipmentProfileStatus) && providerId.equals(g.getProviderId())) {
                    EquipmentsDetail obj = new EquipmentsDetail();
                    if(equipmentsDetailSerial!=null){
                        obj = equipmentsDetailSerial;
                    }

                    StockTransactionSerial e = new StockTransactionSerial(serialFrom, serialFrom, serialFrom, serialFrom, 1l, goodsId
                            , providerId);

                    obj.setReferenCode(transactionActionCode);
                    obj.setSerial(serialFrom);
                    obj.setEquipmentsProfileId(goodsId);
                    obj.setProviderId(providerId);
                    obj.setProviderName(prv.getProviderName());
                    obj.setProviderCode(prv.getProviderCode());
                    obj.setStatus(equipmentProfileStatus);
                    obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
//                    obj.setWarrantyCount(0L);
                    obj.setLifeCycle(litecycle.trim().isEmpty()? null:Long.parseLong(litecycle));
                    obj.setMaintancePeriod(mainteinPrio.trim().isEmpty()? null:Long.parseLong(mainteinPrio));
                    obj.setWarantyExpiredDate(waranDate);
                    obj.setWarrantyPeriod(waranPrio.trim().isEmpty()?null:Long.parseLong(waranPrio));
                    obj.setCoNumber(countCo+"");
                    obj.setCqNumber(countCq+"");
                    obj.setProfileName(profile.getProfileName());
                    obj.setEquipmentsProfileCode(profile.getProfileCode());
                    obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
//                    obj.setLastUsedDate(new Date());
                    obj.setChangWaranNew(changeWaran);
                    if(changeWaran){
                        obj.setId(null);
                        obj.setWarantyStatus(InventoryConstanst.WARAN_STATUS.WARAN_STATUS_2L);
                        obj.setSerialNew(serialNewSingle);
                    }
                    e.setEquipmentDetail(obj);
                    e.setTransactionActionCode(orderCode);
                    e.setProfileManagementType(profile.getManagementType());
                    g.getLsSerial().add(e);
                    if(!serialFrom.trim().isEmpty()) {
                        for (StockGoodsInvSerialDTO stockGoodsInvSerialDTO : lstSerialStockGoods) {
                            if (stockGoodsInvSerialDTO.getSerial().equals(serialFrom.trim()) &&
                                    stockGoodsInvSerialDTO.getEquipmentProfileId().equals(goodsId)) {
                                stockGoodsInvSerialDTO.setChangWaranNew(changeWaran);
                                if(changeWaran){
                                    stockGoodsInvSerialDTO.setStockStatus(InventoryConstanst.StockStatus.CHANGE_WARANTIED);
                                }
                                g.getSetSerial().add(stockGoodsInvSerialDTO);
                                break;
                            }
                        }
                    }
                    g.setGoodsId(profile.getProfileId());
                    g.setUnitType(profile.getUnit());
                    g.setGoodsStatus(equipmentProfileStatus);
                    g.setProviderId(prv.getProviderId());
                    g.setWanranprio(waranPrio.trim().isEmpty()?null:Long.parseLong(waranPrio));
                    g.setWanranExpriDate(waranDate);
                    g.setCountCo(countCo+"");
                    g.setCountCq(countCq+"");
                    g.setOrdercode(orderCode);
                    g.setReferenCode(transactionActionCode);
                    g.setProfileManagementType(profile.getManagementType());

                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        g.setQuantity(Long.parseLong(quantity.trim()));
                        e.setQuantity(g.getQuantity());
                    }else {
                        g.setQuantity(Long.parseLong(g.getLsSerial().size()+""));
                        e.setQuantity(1L);
                    }
                    mapSerrial.put(map, e);
                    k = true;
                    break;
                }

            }
            if (!k) {
                StockTransactionDetail g = new StockTransactionDetail(0L,  profile.getProfileId(), 1l);
                g.setOrdercode(orderCode);
                StockTransactionSerial e = new StockTransactionSerial(serialFrom, serialFrom, serialFrom, serialFrom, 1l, goodsId
                        , providerId);
                EquipmentsDetail obj = new EquipmentsDetail();
                if(equipmentsDetailSerial!=null){
                    obj = equipmentsDetailSerial;
                }
                obj.setReferenCode(transactionActionCode);
                obj.setSerial(serialFrom);
                obj.setEquipmentsProfileId(goodsId);
                obj.setProviderId(providerId);
                obj.setProviderName(prv.getProviderName());
                obj.setProviderCode(prv.getProviderCode());
                obj.setStatus(equipmentProfileStatus);
                obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
//                obj.setWarrantyCount(0L);
                obj.setLifeCycle(litecycle.trim().isEmpty()?null:Long.parseLong(litecycle));
                obj.setMaintancePeriod(mainteinPrio.trim().isEmpty()?null:Long.parseLong(mainteinPrio));
                obj.setWarantyExpiredDate(waranDate);
                obj.setWarrantyPeriod(waranPrio.trim().isEmpty()?null:Long.parseLong(waranPrio));
                obj.setCoNumber(countCo+"");
                obj.setCqNumber(countCq+"");
                obj.setProfileName(profile.getProfileName());
                obj.setEquipmentsProfileCode(profile.getProfileCode());
                obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
//                obj.setLastUsedDate(new Date());
                obj.setChangWaranNew(changeWaran);
                if(changeWaran){
                    obj.setId(null);
                    obj.setSerialNew(serialNewSingle);
                }
                e.setEquipmentDetail(obj);
                e.setProfileManagementType(profile.getManagementType());
                e.setTransactionActionCode(orderCode);
                g.getLsSerial().add(e);
                if(!serialFrom.trim().isEmpty()) {
                    for (StockGoodsInvSerialDTO stockGoodsInvSerialDTO : lstSerialStockGoods) {
                        if (stockGoodsInvSerialDTO.getSerial().equals(serialFrom.trim()) &&
                                stockGoodsInvSerialDTO.getEquipmentProfileId().equals(goodsId)) {
                            stockGoodsInvSerialDTO.setChangWaranNew(changeWaran);
                            if(changeWaran){
                                stockGoodsInvSerialDTO.setStockStatus(InventoryConstanst.StockStatus.CHANGE_WARANTIED);
                            }
                            g.getSetSerial().add(stockGoodsInvSerialDTO);
                            break;
                        }
                    }
                }
                g.setGoodsId(profile.getProfileId());
                g.setUnitType(profile.getUnit());
                g.setGoodsStatus(equipmentProfileStatus);
                g.setProviderId(prv.getProviderId());
                g.setWanranprio(waranPrio.trim().isEmpty()?null:Long.parseLong(waranPrio));
                g.setWanranExpriDate(waranDate);
                g.setCountCo(countCo+"");
                g.setCountCq(countCq+"");
                g.setOrdercode(orderCode);
                g.setReferenCode(transactionActionCode);
                g.setProfileManagementType(profile.getManagementType());
                if(quantity==null||quantity.isEmpty()){
                    g.setQuantity(1L);
                }else {
                    g.setQuantity(Long.parseLong(quantity.trim()));
                }
                if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    g.setQuantity(Long.parseLong(quantity.trim()));
                    e.setQuantity(g.getQuantity());
                }else {
                    g.setQuantity(Long.parseLong(g.getLsSerial().size()+""));
                    e.setQuantity(1L);
                }
//                e.setQuantity(g.getQuantity());
                mapSerrial.put(map, e);
                getLsG().add(g);

            }
            viewClick();
            context.execute("PF('dlgSingle').hide();");
        }catch (Exception ex){
            ex.printStackTrace();
        }
        f5Refresh = false;
    }


    public void handleFileUploadWaranty() throws IOException {
        if(!f5Refresh) return;
        RequestContext context = RequestContext.getCurrentInstance();
        HashMap<String, StockTransactionSerial> mapSerrialBk = new HashMap<>();
        mapSerrialBk.putAll(mapSerrial);
        List<StockTransactionDetail> lsRt = new ArrayList<>();
        if (lsG != null) {
            for (StockTransactionDetail stds : lsG) {
                lsRt.add(stds);
            }
        }
        if (uploadedFile == null) {
            languageBean.showMeseage("Info", "emtyFile");
            context.update("frm:messages");
            return;
        }
        if (!uploadedFile.getFileName().endsWith(".xlsx")) {
            languageBean.showMeseage("Info", "errFileType");
            context.update("frm:messages");
            return;
        }
        int qtt = 0;
        if (providerId==null || goodsId==null || providerId < 0 || goodsId < 0 || quantity.trim().isEmpty()) {
            languageBean.showMeseage("Info", "importReq");
            context.update("frm:messages");
            return;
        }
        if (quantity.matches("[\\d]+$")) {
            qtt = new Integer(quantity);
        } else {
            languageBean.showMeseage("Info", "quantityReqErr");
            context.update("frm:messages");
            return;
        }
        try {
            InputStream file = uploadedFile.getInputstream();
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getLastRowNum();
            if (rowsCount != qtt) {
                Object[] objs = {qtt, rowsCount};
                languageBean.showMeseage("Info", "importReqQuan", objs);
                context.update("frm:messages");
                return;
            }
            Boolean error = false;
            DataFormatter df = new DataFormatter();
            EquipmentsProfile profile = equipmentsProfileService.findByProfileId(goodsId);
            Long totalQuantity = 0L;
            for (int i = 1; i <= rowsCount; i++) {
                Row row = sheet.getRow(i);
                Boolean k = false;

                // Serial
                row.getCell(0);//.setCellType(XSSFCell.CELL_TYPE_STRING);
                String serial = df.formatCellValue(row.getCell(0));
//                 check empty serial
                if (serial == null ||serial.isEmpty()&& InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())) {
                    String s = languageBean.getBundle().getString("importErrorSerial.empty");
//                    s = MessageFormat.format(s, i + 1);
                    // getLanguageBean().showMeseage_("Info", s);
                    error = true;
                    row.createCell(6);
                    row.getCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(6).setCellValue(s);
                    continue;
                }else if(!serial.isEmpty() && !InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    String s = languageBean.getBundle().getString("importErrorSerial.only.empty");
                    error = true;
                    row.createCell(6);
                    row.getCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(6).setCellValue(s);
                    continue;
                }else if( InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType()) &&
                        (equipmentsDetailService.findBySerial(serial.trim())== null
                                || equipmentsDetailService.findByProfileAndSerial(profile.getProfileId(),serial.trim()).isEmpty()))
                {
                    String s = languageBean.getBundle().getString("importErrorSerial.exit.not");
//                    s = MessageFormat.format(s, i + 1);
                    // getLanguageBean().showMeseage_("Info", s);
                    error = true;
                    row.createCell(6);
                    row.getCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(6).setCellValue(s);
                    continue;
                }else if(!serial.isEmpty() ) {
                    boolean dk = false;
                    for (StockTransactionDetail g : getLsG()) {
                        if(!dk) {
                            for (StockTransactionSerial serialE : g.getLsSerial()) {
                                if (serial.equals(serialE.getSerial())) {
                                    String s = "";
                                    if (InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())) {
                                        s = languageBean.getBundle().getString("importErrorSerial.exit.input");
                                    } else {
                                        s = languageBean.getBundle().getString("importErrorSerial.noserial.exit.input");
                                    }
                                    error = true;
                                    row.createCell(6);
                                    row.getCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
                                    row.getCell(6).setCellValue(s);
                                    dk = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(dk) continue;
                }

                if(serial.trim().isEmpty()){
                    boolean dk = false;
                    for (StockTransactionDetail g : getLsG()) {
                        if(!dk) {
                            for (StockTransactionSerial serialE : g.getLsSerial()) {
                                if (goodsId.equals(serialE.getEquipmentProfileId())) {
                                    String s = "";
                                    s = languageBean.getBundle().getString("importErrorSerial.noserial.exit.input");
                                    error = true;
                                    row.createCell(6);
                                    row.getCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
                                    row.getCell(6).setCellValue(s);
                                    dk = true;
                                    break;
                                }
                            }
                        }

                    }
                    if (dk) continue;
                }
                providerId = equipmentsDetailService.findByProfileAndSerial(profile.getProfileId(),serial.trim()).get(0).getProviderId();
                Provider prv = null;
                if (providerId!=null){
                    prv = sessionBean.getProvider(providerId.toString());
                } else {
                    prv = new Provider();
                }

                // Thời gian BH
                row.getCell(1);//.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                Long warranty_time = null;
                String TgBh = df.formatCellValue(row.getCell(1));
                if (TgBh != null && !TgBh.isEmpty() && (!StringUtils.isNumberLong̣̣(TgBh.trim()) || Long.parseLong(TgBh.trim()) < 0 )) {
                    String s = languageBean.getBundle().getString("importWaranPrio");
//                    s = MessageFormat.format(s, i + 1);
                    // getLanguageBean().showMeseage_("Info", s);
                    error = true;
                    row.createCell(6);
                    row.getCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(6).setCellValue(s);
                    continue;
                }else if(TgBh != null && !TgBh.isEmpty()) {
                    warranty_time = Long.parseLong(TgBh.trim());
                }
                // Ngày hết hạn BH
                String warranty_expired_date_str = df.formatCellValue(row.getCell(2)).trim();
                Date warranty_expired_date = null;
                if(warranty_expired_date_str == null || warranty_expired_date_str.isEmpty() || !DateTimeUtils.isValidFormat(warranty_expired_date_str)){
                    String s = languageBean.getBundle().getString("expriceDate.notfomat");
//                    s = MessageFormat.format(s, i + 1);
                    // getLanguageBean().showMeseage_("Info", s);
                    error = true;
                    row.createCell(6);
                    row.getCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(6).setCellValue(s);
                    continue;
                }else if(warranty_expired_date_str!=null && !warranty_expired_date_str.isEmpty()){
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    warranty_expired_date = sdf.parse(warranty_expired_date_str);
//                    warranty_expired_date= row.getCell(2).getDateCellValue();
                    if (warranty_expired_date.compareTo(new Date())<=0){
                        String s = languageBean.getBundle().getString("expriceDate.than.today");
//                    s = MessageFormat.format(s, i + 1);
                        // getLanguageBean().showMeseage_("Info", s);
                        error = true;
                        row.createCell(6);
                        row.getCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(6).setCellValue(s);
                    }
                }
                row.getCell(3);//.setCellType(XSSFCell.CELL_TYPE_STRING);
                String maintance_period = df.formatCellValue(row.getCell(3));
                Long maintance_periodL = null;
                if (maintance_period != null && !maintance_period.isEmpty() && (!StringUtils.isNumberLong̣̣(maintance_period.trim()) || Long.parseLong(maintance_period.trim()) < 0 )) {
                    String s = languageBean.getBundle().getString("maintance_period.notfomat");
                    s = MessageFormat.format(s, i + 1);
                    // getLanguageBean().showMeseage_("Info", s);
                    error = true;
                    row.createCell(6);
                    row.getCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(6).setCellValue(s);
                    continue;
                }else if(maintance_period != null && !maintance_period.isEmpty()) {
                    maintance_periodL = Long.parseLong(maintance_period) ;
                }
                // Vòng đời(tháng)
                row.getCell(4);//.setCellType(XSSFCell.CELL_TYPE_STRING);
                String life_cycle = df.formatCellValue(row.getCell(4)).trim();
                Long life_cycleL = null;
                if (life_cycle != null && !life_cycle.isEmpty() && (!StringUtils.isNumberLong̣̣(life_cycle.trim()) || Long.parseLong(life_cycle.trim()) < 0 )) {
                    String s = languageBean.getBundle().getString("life_cycle.notfomat");
//                    s = MessageFormat.format(s, i + 1);
                    // getLanguageBean().showMeseage_("Info", s);
                    error = true;
                    row.createCell(6);
                    row.getCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(6).setCellValue(s);
                    continue;
                }else if(life_cycle != null && !life_cycle.isEmpty()){
                    life_cycleL = Long.parseLong(life_cycle);
                }
                // Số lượng
                row.getCell(5);//.setCellType(XSSFCell.CELL_TYPE_STRING);
                String quantityIP = df.formatCellValue(row.getCell(5)).trim();
                Long quantityIPL = 1L;
                if (quantityIP != null && !quantityIP.isEmpty() && (!StringUtils.isNumberLong̣̣(quantityIP.trim()) || Long.parseLong(quantityIP.trim()) < 0 )) {
                    String s ="";
                    if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        s = languageBean.getBundle().getString("quantityIP.notfomat.default");
                    }else {
                        s = languageBean.getBundle().getString("quantityIP.notfomat");
                    }
                    error = true;
                    row.createCell(6);
                    row.getCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(6).setCellValue(s);
                    continue;
                }else if(quantityIP != null && !quantityIP.isEmpty()){
                    quantityIPL = Long.parseLong(quantityIP);
                    String s ="";
                    if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())
                            && quantityIPL > 1){
                        s = languageBean.getBundle().getString("quantityIP.notfomat.default");
                        error = true;
                        row.createCell(6);
                        row.getCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(6).setCellValue(s);
                        continue;
                    }
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        totalQuantity+=quantityIPL;
                    }

                }
                String map = serial + "-" + profile.getProfileId().toString() + "-" + prv.getProviderId().toString();
                StockTransactionSerial std = mapSerrial.get(map);
                // Check duplicate serial, provider, good_id in file
                if (std != null) {
                    String s = languageBean.getBundle().getString("errFile");
                    s = MessageFormat.format(s, serial);
                    error = true;
                    row.createCell(6);
                    row.getCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(6).setCellValue(s);
                    continue;
                }

                StockGoodsInvSerialDTO serialOLd = null;
                EquipmentsDetail detailOld = new EquipmentsDetail();
                if(!serial.isEmpty()){
                    for (StockGoodsInvSerialDTO stockGoodsInvSerialDTO:lstSerialStockGoods) {
                        if(stockGoodsInvSerialDTO.getSerial().equals(serial) &&
                                stockGoodsInvSerialDTO.getEquipmentProfileId().equals(goodsId)){
                            detailOld = equipmentsDetailService.findById(stockGoodsInvSerialDTO.getEquimentDetailId());
                            serialOLd = stockGoodsInvSerialDTO;
                            break;
                        }
                    }
                }else {
                    List<EquipmentsDetail> eDetail = equipmentsDetailService.findByProfile(profile.getProfileId());
                    if(eDetail!=null && !eDetail.isEmpty()) {
                        detailOld = eDetail.get(0);
                    }
                }

                for (StockTransactionDetail g : getLsG()) {
                    if (goodsId.equals(g.getGoodsId()) && providerId.equals(g.getProviderId())
                            && detailOld.getStatus().equals(g.getGoodsStatus())) {
                        EquipmentsDetail obj = new EquipmentsDetail();
                        StockTransactionSerial e = new StockTransactionSerial(serial, serial, serial, serial, g.getGoodsStatus(), profile.getProfileId()
                                , prv.getProviderId());
                        if(!serial.isEmpty() && serialOLd!=null){
                            g.getSetSerial().add(serialOLd);
//                            for (StockGoodsInvSerialDTO stockGoodsInvSerialDTO:lstSerialStockGoods) {
//                                if(stockGoodsInvSerialDTO.getSerial().equals(serial) &&
//                                        stockGoodsInvSerialDTO.getEquipmentProfileId().equals(goodsId)){
//                                    obj = equipmentsDetailService.findById(stockGoodsInvSerialDTO.getEquimentDetailId());
//                                    g.getSetSerial().add(stockGoodsInvSerialDTO);
//                                    break;
//                                }
//                            }
                        }
//                        else {
//                            List<EquipmentsDetail> eDetail = equipmentsDetailService.findByProfile(profile.getProfileId());
//                            if(eDetail!=null && !eDetail.isEmpty()) {
//                                obj = eDetail.get(0);
//                            }
//                        }
                        if(detailOld!=null){
                            obj = detailOld;
                            obj.setStatusOld(detailOld.getStatus());
                        }

                        obj.setReferenCode(transactionActionCode);
                        obj.setSerial(serial);
                        obj.setEquipmentsProfileId(profile.getProfileId());
                        obj.setProviderId(prv.getProviderId());
                        obj.setProviderName(prv.getProviderName());
                        obj.setProviderCode(prv.getProviderCode());
                        obj.setStatus(equipmentProfileStatus);
                        obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
                        obj.setLifeCycle(life_cycleL);
                        obj.setMaintancePeriod(maintance_periodL);
                        obj.setWarrantyPeriod(warranty_time);
                        obj.setWarantyExpiredDate(warranty_expired_date);
//                        obj.setCoNumber(co);
//                        obj.setCqNumber(cq);
                        obj.setProfileName(profile.getProfileName());
                        obj.setEquipmentsProfileCode(profile.getProfileCode());
                        obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
//                        obj.setLastUsedDate(new Date());
                        e.setEquipmentDetail(obj);
                        e.setTransactionActionCode(orderCode);
                        g.getLsSerial().add(e);
                        g.setGoodsId(profile.getProfileId());
                        g.setUnitType(profile.getUnit());
                        g.setGoodsStatus(equipmentProfileStatus);
                        g.setProviderId(prv.getProviderId());
                        g.setWanranprio(warranty_time);
                        g.setMaintancePeriod(maintance_periodL);
                        g.setWanranExpriDate(warranty_expired_date);
//                        g.setCountCo(co);
//                        g.setCountCq(cq);
                        g.setReferenCode(transactionActionCode);
//                        if(quantity==null||quantity.isEmpty()){
//                            g.setQuantity(1L);
//                        }else {
//                            g.setQuantity(Long.parseLong(quantity.trim()));
//                        }
                        if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                            g.setQuantity(totalQuantity);
                            e.setQuantity(quantityIPL);
                        }else {
                            g.setQuantity(Long.parseLong(g.getLsSerial().size()+""));
                            e.setQuantity(1L);
                        }
                        g.setProfileManagementType(profile.getManagementType());
                        mapSerrial.put(map, e);
                        k = true;
                        break;
                    }
                }
                if (!k) {
                    StockTransactionDetail g = new StockTransactionDetail(0L,  profile.getProfileId(), detailOld.getStatus());
                    g.setOrdercode(orderCode);
//                    g.setProfileName(goods.getEquipProfileName());
//                    g.setGoodsGroupName(sessionBean.getEquipmentGroupName(goods.getGoodsGroupId()).getGoodsGroupName());
                    StockTransactionSerial e = new StockTransactionSerial(serial, serial, serial, serial, detailOld.getStatus(),  profile.getProfileId(),
                            prv.getProviderId());
//                    g.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(g.getGoodsStatus().toString()));
//                    e.setProfileName(goods.getEquipProfileName());
                    EquipmentsDetail obj = new EquipmentsDetail();
                    if(!serial.isEmpty()){
                        for (StockGoodsInvSerialDTO stockGoodsInvSerialDTO:lstSerialStockGoods) {
                            if(stockGoodsInvSerialDTO.getSerial().equals(serial) &&
                                    stockGoodsInvSerialDTO.getEquipmentProfileId().equals(goodsId)){
                                obj = equipmentsDetailService.findById(stockGoodsInvSerialDTO.getEquimentDetailId());
                                obj.setStatusOld(obj.getStatus());
                                g.getSetSerial().add(stockGoodsInvSerialDTO);
                                break;
                            }
                        }
                    }else {
                        List<EquipmentsDetail> eDetail = equipmentsDetailService.findByProfile(profile.getProfileId());
                        if(eDetail!=null && !eDetail.isEmpty()) {
                            obj = eDetail.get(0);
                            obj.setStatusOld(obj.getStatus());
                        }
                    }
                    if(obj==null){
                        obj = new EquipmentsDetail();
                    }
                    obj.setReferenCode(transactionActionCode);
                    obj.setSerial(serial);
                    obj.setEquipmentsProfileId(profile.getProfileId());
                    obj.setProviderId(prv.getProviderId());
                    obj.setProviderName(prv.getProviderName());
                    obj.setProviderCode(prv.getProviderCode());
                    obj.setStatus(equipmentProfileStatus);
                    obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
                    obj.setLifeCycle(life_cycleL);
                    obj.setMaintancePeriod(maintance_periodL);
                    obj.setWarantyExpiredDate(warranty_expired_date);
                    obj.setWarrantyPeriod(warranty_time);
//                    obj.setCoNumber(co);
//                    obj.setCqNumber(cq);
                    obj.setProfileName(profile.getProfileName());
                    obj.setEquipmentsProfileCode(profile.getProfileCode());
                    obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
//                    obj.setLastUsedDate(new Date());
                    e.setEquipmentDetail(obj);
                    e.setTransactionActionCode(orderCode);
                    g.getLsSerial().add(e);
                    g.setGoodsId(profile.getProfileId());
                    g.setUnitType(profile.getUnit());
                    g.setGoodsStatus(equipmentProfileStatus);
                    g.setProviderId(prv.getProviderId());
                    g.setWanranprio(warranty_time);
                    g.setMaintancePeriod(maintance_periodL);
                    g.setWanranExpriDate(warranty_expired_date);
//                    g.setCountCo(co);
//                    g.setCountCq(cq);
                    g.setReferenCode(transactionActionCode);
//                    if(quantity==null||quantity.isEmpty()){
//                        g.setQuantity(1L);
//                    }else {
//                        g.setQuantity(Long.parseLong(quantity.trim()));
//                    }
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        g.setQuantity(totalQuantity);
                        e.setQuantity(quantityIPL);
                    }else {
                        g.setQuantity(Long.parseLong(g.getLsSerial().size()+""));
                        e.setQuantity(1L);
                    }
                    g.setProfileManagementType(profile.getManagementType());
                    mapSerrial.put(map, e);
                    getLsG().add(g);
                }

            }
            f5Refresh = false;
            if (error) {
                f5Refresh = true;
                lsG = lsRt;
                mapSerrial.clear();
                mapSerrial.putAll(mapSerrialBk);
//                reload();
                getLanguageBean().showMeseage("Info", "errorReadFile");
                String k = new Long(new Date().getTime()).toString();
                FileOutputStream out = new FileOutputStream(k + ".xlsx");
                workbook.write(out);
                File f = new File(k + ".xlsx");
                downloadFile(f);
                out.flush();
                out.close();

            }
            file.close();
//            uploadedFile = null;
            // context.execute("PF('dlg').show();");
        } catch (Exception ex) {
            ex.printStackTrace();
            lsG = lsRt;
//            reload();
            getLanguageBean().showMeseage("Info", "errFileData");
        }
    }

    public void handleFileUploadKTV() throws IOException {
        if(!f5Refresh) return;
        RequestContext context = RequestContext.getCurrentInstance();
        HashMap<String, StockTransactionSerial> mapSerrialBk = new HashMap<>();
        mapSerrialBk.putAll(mapSerrial);
        List<StockTransactionDetail> lsRt = new ArrayList<>();
        if (lsG != null) {
            for (StockTransactionDetail stds : lsG) {
                lsRt.add(stds);
            }
        }
        if (uploadedFile == null) {
            languageBean.showMeseage("Info", "emtyFile");
            context.update("frm:messages");
            return;
        }
        if (!uploadedFile.getFileName().endsWith(".xlsx")) {
            languageBean.showMeseage("Info", "errFileType");
            context.update("frm:messages");
            return;
        }
//        int qtt = 0;
//        if (providerId==null || goodsId==null || providerId < 0 || goodsId < 0 || quantity.trim().isEmpty()) {
//            languageBean.showMeseage("Info", "importReq");
//            context.update("frm:messages");
//            return;
//        }
//        if (quantity.matches("[\\d]+$")) {
//            qtt = new Integer(quantity);
//        } else {
//            languageBean.showMeseage("Info", "quantityReqErr");
//            context.update("frm:messages");
//            return;
//        }
        try {
            InputStream file = uploadedFile.getInputstream();
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getLastRowNum();
            if (rowsCount < 1) {
//                Object[] objs = {qtt, rowsCount};
                languageBean.showMeseage("Info", "import.not.empty");
                context.update("frm:messages");
                return;
            }
            lstSerialStockGoodsAll = businessService.getSerialStockGoodsImportKTV(null, null,
                    null, InventoryConstanst.StockStatus.EX_USED);
            Boolean error = false;
//            Provider prv = sessionBean.getProvider(providerId.toString());
//            sheet.getRow(0).getCell(4).setCellType(XSSFCell.CELL_TYPE_STRING);
//            sheet.getRow(0).getCell(4).setCellValue("Kết quả kiểm tra");
            DataFormatter df = new DataFormatter();
//            EquipmentsProfile profile = equipmentsProfileService.findByProfileId(goodsId);
            Long totalQuantity = 0L;
            int endCel = 3;
            for (int i = 1; i <= rowsCount; i++) {
                Row row = sheet.getRow(i);
                Boolean k = false;
//NCC
                int column = 0;
                Provider prv = null;
                EquipmentsProfile profile = null;
//                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
//                String providerCode = df.formatCellValue(row.getCell(column)).trim();
//                if (providerCode != null && !providerCode.isEmpty()) {
//                    prv = providerService.findByProviderCode(providerCode);
//                    if(prv==null){
//                        String s = languageBean.getBundle().getString("import.ncc.not.exit" );
//                        s = MessageFormat.format(s, providerCode);
//                        error = true;
//                        row.createCell(endCel);
//                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
//                        row.getCell(endCel).setCellValue(s);
//                        continue;
//                    }
//                    providerId = prv.getProviderId();
//
//                }else {
//                    String s = languageBean.getBundle().getString("import.ncc.not.input" );
//                    error = true;
//                    row.createCell(endCel);
//                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
//                    row.getCell(endCel).setCellValue(s);
//                    continue;
//                }
//                column++;
                //Ma TB
                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                String profileCode = df.formatCellValue(row.getCell(column)).trim();
                if (profileCode != null && !profileCode.isEmpty()) {
                    profile = equipmentsProfileService.checkExists(profileCode);
                    if(profile==null){
                        String s = languageBean.getBundle().getString("import.profileCode.not.exit" );
                        s = MessageFormat.format(s, profileCode);
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                        continue;
                    }else if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(profile.getEquipProfileStatus())){
                        String s = languageBean.getBundle().getString("profilecode.inactive.not.action" );
                        s = MessageFormat.format(s, profileCode);
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                        continue;
                    }
                    goodsId = profile.getProfileId();
                }else {
                    String s = languageBean.getBundle().getString("import.profile.not.input" );
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(profile.getEquipProfileStatus())){
                    String s = languageBean.getBundle().getString("import.profile.not.active" );
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }
                column++;
                // Serial
                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_STRING);
                String serial = df.formatCellValue(row.getCell(column));
//                 check empty serial
                if ((serial == null || serial.isEmpty())&& InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())) {
                    String s = languageBean.getBundle().getString("importErrorSerial.empty");
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }else if(!serial.isEmpty() && !InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    String s = languageBean.getBundle().getString("importErrorSerial.only.empty");
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }else if( InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType()) &&
                        (equipmentsDetailService.findBySerial(serial.trim())== null
                        || equipmentsDetailService.findByProfileAndSerial(profile.getProfileId(),serial.trim()).isEmpty()))
                {
                    String s = languageBean.getBundle().getString("importErrorSerial.exit.not");
//                    s = MessageFormat.format(s, i + 1);
                    // getLanguageBean().showMeseage_("Info", s);
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }else if(!serial.isEmpty() ) {
                    boolean ch = false;
                    for (StockTransactionDetail g : getLsG()) {
                        if(!ch) {
                            for (StockTransactionSerial serialE : g.getLsSerial()) {
                                if (serial.equals(serialE.getSerial())) {
                                    String s = "";
                                    if (InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())) {
                                        s = languageBean.getBundle().getString("importErrorSerial.exit.input");
                                    } else {
                                        s = languageBean.getBundle().getString("importErrorSerial.noserial.exit.input");
                                    }
                                    error = true;
                                    row.createCell(endCel);
                                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                                    row.getCell(endCel).setCellValue(s);
                                    ch = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(ch) continue;

                    boolean dk = false;
                    for (StockGoodsInvSerialDTO stockGoodsInvSerialDTO:lstSerialStockGoodsAll) {
//                        if(InventoryConstanst.ImportType.importKTV.equals(handleType)
//                                && !sessionBean.getStaff().getStaffId().equals(stockGoodsInvSerialDTO.getStaffId())){
//                            continue;
//                        }
                        if(serial.trim().equals(stockGoodsInvSerialDTO.getSerial()) &&
                                stockGoodsInvSerialDTO.getEquipmentProfileId().equals(profile.getProfileId())
                            ){
                            dk = true;
                            break;
                        }
                    }
                    if(!dk){
                        String s = languageBean.getBundle().getString("importErrorSerial.not.exit.ktv");
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                        continue;
                    }
                }
                if(serial.trim().isEmpty()){
                    boolean dk = false;
                    for (StockTransactionDetail g : getLsG()) {
                        if(!dk) {
                            for (StockTransactionSerial serialE : g.getLsSerial()) {
                                if (goodsId.equals(serialE.getEquipmentProfileId())) {
                                    String s = "";
                                    s = languageBean.getBundle().getString("importErrorSerial.noserial.exit.input");
                                    error = true;
                                    row.createCell(6);
                                    row.getCell(6).setCellType(XSSFCell.CELL_TYPE_STRING);
                                    row.getCell(6).setCellValue(s);
                                    dk = true;
                                    break;
                                }
                            }
                        }

                    }
                    if (dk) continue;
                }
                column++;

                EquipmentsDetail detailS = null;
                if(!serial.isEmpty()){
                    for (StockGoodsInvSerialDTO stockGoodsInvSerialDTO:lstSerialStockGoodsAll) {
                        if(stockGoodsInvSerialDTO.getSerial().equals(serial) &&
                                stockGoodsInvSerialDTO.getEquipmentProfileId().equals(goodsId)){
                            detailS = equipmentsDetailService.findById(stockGoodsInvSerialDTO.getEquimentDetailId());
                            break;
                        }
                    }
                }else {
                    List<EquipmentsDetail> eDetail = equipmentsDetailService.findByProfile(profile.getProfileId());
                    if(eDetail!=null && !eDetail.isEmpty()) {
                        detailS = eDetail.get(0);
                    }
                }
                if(detailS!=null){
                    providerId = detailS.getProviderId();
                }
                if (providerId!=null){
                    prv = sessionBean.getProvider(providerId.toString());
                } else {
                    prv = new Provider();
                }
                // Số lượng
                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_STRING);
                String quantityIP = df.formatCellValue(row.getCell(column)).trim();
                Long quantityIPL = 1L;
                if (quantityIP != null && !quantityIP.isEmpty()
                        && (!StringUtils.isNumberLong̣̣(quantityIP.trim()) || Long.parseLong(quantityIP.trim()) < 0 )) {
                    String s ="";
                    if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        s = languageBean.getBundle().getString("quantityIP.notfomat.default");
                    }else {
                        s = languageBean.getBundle().getString("quantityIP.notfomat");
                    }
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }else if(quantityIP != null && !quantityIP.isEmpty()){
                    quantityIPL = Long.parseLong(quantityIP);
                    String s ="";
                    if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType()) && quantityIPL > 1){
                        s = languageBean.getBundle().getString("quantityIP.notfomat.default");
                        error = true;
                        row.createCell(endCel);
                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                        row.getCell(endCel).setCellValue(s);
                        continue;
                    }
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                        totalQuantity+=quantityIPL;
                    }

                }
//                column++;
                // TRạng thái
//                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_STRING);
//                String statusNameIM = df.formatCellValue(row.getCell(column)).trim();
//                Long equipStatus = -1L;
//                if (statusNameIM == null  || statusNameIM.isEmpty()) {
//                    String s ="";
//                    s = languageBean.getBundle().getString("status.good.not.empty");
//                    error = true;
//                    row.createCell(endCel);
//                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
//                    row.getCell(endCel).setCellValue(s);
//                    continue;
//                }else{
//                    for (ApDomain apd:sessionBean.getListStatus()) {
//                        if(statusNameIM.toUpperCase().trim().equals(apd.getName().toUpperCase().trim())){
//                            equipStatus = apd.getValue();
//                            break;
//                        }
//                    }
//                    if(equipStatus < 0){
//                        String s = languageBean.getBundle().getString("status.good.not.fomat");
//                        error = true;
//                        row.createCell(endCel);
//                        row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
//                        row.getCell(endCel).setCellValue(s);
//                        continue;
//                    }
//                }
//                column++;
                // PXK
//                row.getCell(column);//.setCellType(XSSFCell.CELL_TYPE_STRING);
//                String pxkCode = df.formatCellValue(row.getCell(column)).trim();
//
                String pxkCode = "";
                String map = serial + "-" + profile.getProfileId().toString() + "-" + prv.getProviderId().toString();
                StockTransactionSerial std = mapSerrial.get(map);
                // Check duplicate serial, provider, good_id in file
                if (std != null) {
                    String s = languageBean.getBundle().getString("errFile");
                    s = MessageFormat.format(s, serial);
                    error = true;
                    row.createCell(endCel);
                    row.getCell(endCel).setCellType(XSSFCell.CELL_TYPE_STRING);
                    row.getCell(endCel).setCellValue(s);
                    continue;
                }
                Long equipStatus = -1L;
                StockGoodsInvSerialDTO serialOLd = null;
                EquipmentsDetail detailOld = new EquipmentsDetail();
                if(!serial.isEmpty()){
                    for (StockGoodsInvSerialDTO stockGoodsInvSerialDTO:lstSerialStockGoodsAll) {
                        if(stockGoodsInvSerialDTO.getSerial().equals(serial) &&
                                stockGoodsInvSerialDTO.getEquipmentProfileId().equals(goodsId)){
                            detailOld = equipmentsDetailService.findById(stockGoodsInvSerialDTO.getEquimentDetailId());
                            serialOLd = stockGoodsInvSerialDTO;
                            if(InventoryConstanst.ImportType.importWarranty.equals(handleType)){
                                for (int dem = 0; i < lstTransactionAction.size(); dem++) {
                                    TransactionAction skin = lstTransactionAction.get(dem);
                                    if (skin.getTransactionActionId().equals(serialOLd.getTransactionActionId())) {
                                        pxkCode = skin.getTransactionActionCode();
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }else {
                    List<EquipmentsDetail> eDetail = equipmentsDetailService.findByProfile(profile.getProfileId());
                    if(eDetail!=null && !eDetail.isEmpty()) {
                        detailOld = eDetail.get(0);
                    }
                }
//                && providerId.equals(g.getProviderId()) && g.getGoodsStatus().equals(equipmentProfileStatus)
                for (StockTransactionDetail g : getLsG()) {
                    if (goodsId.equals(g.getGoodsId()) && providerId.equals(g.getProviderId())
                            && g.getGoodsStatus().equals(detailOld.getStatus())) {
                        EquipmentsDetail obj = new EquipmentsDetail();
                        if(!serial.isEmpty() && serialOLd!=null){
                            g.getSetSerial().add(serialOLd);
//                            for (StockGoodsInvSerialDTO stockGoodsInvSerialDTO:lstSerialStockGoodsAll) {
//                                if(stockGoodsInvSerialDTO.getSerial().equals(serial) &&
//                                        stockGoodsInvSerialDTO.getEquipmentProfileId().equals(goodsId)){
//                                    obj = equipmentsDetailService.findById(stockGoodsInvSerialDTO.getEquimentDetailId());
//                                    g.getSetSerial().add(stockGoodsInvSerialDTO);
//                                    break;
//                                }
//                            }
                        }
                        if(detailOld!=null){
                            obj = detailOld;
                        }
                        equipStatus = obj.getStatus();
                        StockTransactionSerial e = new StockTransactionSerial(serial, serial, serial, serial, 1l, profile.getProfileId()
                                , prv.getProviderId());
//                        StockTransactionSerial staSerial = businessService.getStockSerials(serial,profile.getProfileId() );
//                        todo: lam tiep
                        if(!pxkCode.isEmpty()){
                            obj.setReferenCode(pxkCode);
                        }
                        obj.setSerial(serial);
                        obj.setEquipmentsProfileId(profile.getProfileId());
                        obj.setProviderId(prv.getProviderId());
                        obj.setProviderName(prv.getProviderName());
                        obj.setProviderCode(prv.getProviderCode());
                        obj.setStatus(equipStatus);
                        obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
                        obj.setProfileName(profile.getProfileName());
                        obj.setEquipmentsProfileCode(profile.getProfileCode());
                        obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
//                        obj.setLastUsedDate(new Date());
                        e.setEquipmentDetail(obj);
                        e.setTransactionActionCode(orderCode);
//                        g.getLsSerial().add(e);
                        g.setGoodsId(profile.getProfileId());
                        g.setUnitType(profile.getUnit());
                        g.setGoodsStatus(equipStatus);
                        g.setProviderId(prv.getProviderId());
                        g.setWanranprio(obj.getWarrantyPeriod());
                        g.setMaintancePeriod(obj.getMaintancePeriod());
                        g.setWanranExpriDate(obj.getWarantyExpiredDate());
                        if(!pxkCode.isEmpty()){
                            g.setReferenCode(pxkCode);
                        }
                        g.setProfileManagementType(profile.getManagementType());
                        if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
//                            g.setQuantity(totalQuantity);
                            if(g.getLsSerial().isEmpty()){
                                g.getLsSerial().add(e);
                            }
                            g.setQuantity(g.getQuantity() + quantityIPL);
                            e.setQuantity(g.getQuantity() + quantityIPL);
                        }else {
                            g.getLsSerial().add(e);
                            g.setQuantity(Long.parseLong(g.getLsSerial().size()+""));
                            e.setQuantity(1L);
                        }
                        g.setProfileManagementType(profile.getManagementType());
                        mapSerrial.put(map, e);
                        k = true;
                        break;
                    }
                }
                if (!k) {
                    StockTransactionDetail g = new StockTransactionDetail(0L,  profile.getProfileId(), 1l);
                    g.setOrdercode(orderCode);
//                    g.setProfileName(goods.getEquipProfileName());
//                    g.setGoodsGroupName(sessionBean.getEquipmentGroupName(goods.getGoodsGroupId()).getGoodsGroupName());
                    StockTransactionSerial e = new StockTransactionSerial(serial, serial, serial, serial, 1l,  profile.getProfileId(),
                            prv.getProviderId());
//                    g.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(g.getGoodsStatus().toString()));
//                    e.setProfileName(goods.getEquipProfileName());
                    EquipmentsDetail obj = new EquipmentsDetail();
                    if(!serial.isEmpty() && serialOLd!=null){
                        g.getSetSerial().add(serialOLd);
                    }
                    if(detailOld!=null){
                        obj = detailOld;
                    }
                    equipStatus = obj.getStatus();

                    if(!pxkCode.isEmpty()){
                        obj.setReferenCode(pxkCode);
                    }
                    obj.setSerial(serial);
                    obj.setEquipmentsProfileId(profile.getProfileId());
                    obj.setProviderId(prv.getProviderId());
                    obj.setProviderName(prv.getProviderName());
                    obj.setProviderCode(prv.getProviderCode());
                    obj.setStatus(equipStatus);
                    obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
                    obj.setProfileName(profile.getProfileName());
                    obj.setEquipmentsProfileCode(profile.getProfileCode());
                    obj.setEquipmentsGroupId(profile.getEquipmentsGroupId());
//                    obj.setLastUsedDate(new Date());
                    e.setEquipmentDetail(obj);
                    e.setTransactionActionCode(orderCode);
//                    g.getLsSerial().add(e);
                    g.setGoodsId(profile.getProfileId());
                    g.setUnitType(profile.getUnit());
                    g.setGoodsStatus(equipStatus);
                    g.setProviderId(prv.getProviderId());
                    g.setProviderId(prv.getProviderId());
                    g.setWanranprio(obj.getWarrantyPeriod());
                    g.setMaintancePeriod(obj.getMaintancePeriod());

                    if(!pxkCode.isEmpty()){
                        g.setReferenCode(pxkCode);
                    }
                    g.setProfileManagementType(profile.getManagementType());
                    if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
//                        g.setQuantity(totalQuantity);
                        if(g.getLsSerial().isEmpty()){
                            g.getLsSerial().add(e);
                        }
                        g.setQuantity(quantityIPL);
                        e.setQuantity(quantityIPL);
                    }else {
                        g.getLsSerial().add(e);
                        g.setQuantity(Long.parseLong(g.getLsSerial().size()+""));
                        e.setQuantity(1L);
                    }
                    g.setProfileManagementType(profile.getManagementType());
                    mapSerrial.put(map, e);
                    getLsG().add(g);
                }

            }
            f5Refresh = false;
            if (error) {
                f5Refresh = true;
                lsG = lsRt;
                mapSerrial.clear();
                mapSerrial.putAll(mapSerrialBk);
//                reload();
                getLanguageBean().showMeseage("Info", "errorReadFile");
                String k = new Long(new Date().getTime()).toString();
                FileOutputStream out = new FileOutputStream(k + ".xlsx");
                workbook.write(out);
                File f = new File(k + ".xlsx");
                downloadFile(f);
                out.flush();
                out.close();
            }
            file.close();
//            uploadedFile = null;
            // context.execute("PF('dlg').show();");
        } catch (Exception ex) {
            ex.printStackTrace();
            lsG = lsRt;
//            reload();
            getLanguageBean().showMeseage("Info", "errFileData");
        }
//        f5Refresh = false;
//        context.execute("document.getElementById('frm:btnClose').click(); ");
    }

    public void refreshAction(){
    }

    private void downloadFile(File file) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setHeader("Content-Disposition", "attachment;filename=ketqua-kiemtra.xlsx");
        response.setContentLength((int) file.length());
        FileInputStream input = null;
        try {
            int i = 0;
            input = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            while ((i = input.read(buffer)) != -1) {
                response.getOutputStream().write(buffer);
                response.getOutputStream().flush();
            }
            facesContext.responseComplete();
            facesContext.renderResponse();

        } catch (IOException e) {
            e.printStackTrace();
            facesContext.responseComplete();
            facesContext.renderResponse();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                response.getOutputStream().close();
                facesContext.responseComplete();
                PrintWriter out=response.getWriter();
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showImportPopUp() {
        RequestContext context = RequestContext.getCurrentInstance();
        lsProvider = sessionBean.getService().getActiveProvider();
        goodsId = null;
        goods = "";
//        serialFrom = "";
//        providerId = null;
        context.execute("PF('dlg').show();");
    }
    
    public void showImportSinglePopUp() {
        RequestContext context = RequestContext.getCurrentInstance();
        lsProvider = sessionBean.getService().getActiveProvider();
        goodsId = null;
        goods = "";
//        serialFrom = "";
//        quantity  = "";
//        providerId = null;
//        countCo = "";
//        countCq ="";
//        waranPrio = "";
//        waranDate = null;
//        mainteinPrio = "";
//        litecycle ="";
        context.execute("PF('dlgSingle').show();");
    }

    public void print() throws Exception {
        try {
            if (lsG != null && !lsG.isEmpty()) {
                RequestContext context = RequestContext.getCurrentInstance();
                ExportInventory ex = new ExportInventory();
                path = ex.export(lsG);
                //  path = new DefaultStreamedContent(new FileInputStream("E:\\vofficeUpload\\expVETC\\printGoods1444704793752.pdf"), "application/pdf");
                Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
                session.put("dlgPDF", path);
                context.execute("PF('dlgPDF').show();");
            }
        } catch (Exception ex) {
            languageBean.showMeseage("Info", "errorProcess");
        }
    }

    public void print_() throws Exception {
        try {
            if (lsG != null && !lsG.isEmpty()) {
                RequestContext context = RequestContext.getCurrentInstance();
                ExportInventory ex = new ExportInventory();
                for (StockTransactionDetail s : lsG) {
                    s.setGoodsName(sessionBean.getEquipProfileName(s.getGoodsId().toString()));
                    s.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(s.getGoodsStatus()));
//                    s.setQuantity(s.getLsSerial().size() + 0L);
                }
                SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                String tranTypeName = "";
                if(InventoryConstanst.ImportType.importGood.equals(handleType)){
                    tranTypeName = languageBean.getBundle().getString("importGood.tran.name");
                }else if(InventoryConstanst.ImportType.importWarranty.equals(handleType)){
                    tranTypeName = languageBean.getBundle().getString("importWarranty.tran.name");
                }else if(InventoryConstanst.ImportType.importKTV.equals(handleType)){
                    tranTypeName = languageBean.getBundle().getString("importKTV.tran.name");
                }else {
                    tranTypeName = languageBean.getBundle().getString("importGood.tran.name");
                }
                path = ex.importPrint(lsG, orderCode, sessionBean.getShop().getShopName(), usser, sf.format(new Date()), tranTypeName);
                //  path = new DefaultStreamedContent(new FileInputStream("E:\\vofficeUpload\\expVETC\\printGoods1444704793752.pdf"), "application/pdf");
                Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
                session.put("dlgPDF", path);
                context.execute("PF('dlgPDF').show();");
            }
        } catch (Exception ex) {
            languageBean.showMeseage("Info", "errorProcess");
        }
    }

    public void exp() throws Exception {
        ExportInventory ex = new ExportInventory();
        ex.importExp(lsG);
    }

    public void selectProvider() {
//        ProviderDAO pvDAO = new ProviderDAO();
//        Provider p = pvDAO.findById(providerId);
//        if (p.getStatus().equals("0")) {
//            providerId = null;
//            languageBean.showMeseage("Info", "providerInactive");
//            lsProvider = sessionBean.getService().getActiveProvider();
//        }
    }
    public void selectEquipType() {
        lsgGroup.clear();
        stockTransactionSerialId = null;
        serialSingle = "";
        if(equipType!=null){
            lsgGroup = getGoodsGroupByType(equipType);
            goodsGroup = null;
            if(lsgGroup!=null && !lsgGroup.isEmpty()){
                goodsGroup = lsgGroup.get(0).getEquipmentsGroupId();
            }
            goods = "";
            goodsId = 0L;
            changeGoodsGroup();
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("frmDlg:autoGoodsSingle");
            context.update("frm:autoGoodsFile");
            disableQuantity = false;
//            if(equipType.equals(1L)){
//                disableQuantity = true;
////                serialTitle = "Serial(*)";
//                quantity = "1";
//            }else {
//                disableQuantity = false;
////                serialTitle = "Serial";
//            }
            initInputSerial();
            context.update("frmDlg:serialTitle");
//            if()
            context.update("frmDlg:quantitySingle");
            context.update("frmDlg:serialSingle");
        }
    }

    public List<EquipmentsGroup>  getGoodsGroupByType(Long equipType) {
        List<EquipmentsGroup> objs = new ArrayList<>();
        List<EquipmentsGroup> lsAll  = sessionBean.getLsgGroupOrder();
        if (lsAll != null && equipType != null) {
            for (EquipmentsGroup g : lsAll) {
                if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipmentsGroupStatus())){
                    continue;
                }
                if (equipType.equals(g.getEquipmentsGroupType())) {
                    objs.add(g);
                }
            }
        }
        return objs;
    }

    public void searchDetail() {
        if (lsG == null || lsG.isEmpty()) {
            languageBean.showMeseage("Info", "importReqFile");
            return;
        }
        if (getStockDetail() == null) {
            languageBean.showMeseage("Info", "importReqView");
            return;
        }
        List<StockTransactionSerial> lsIn = new ArrayList<>();
//        if (fromSerial != null && !fromSerial.trim().isEmpty() && !fromSerial.matches("^[0-9A-Fa-f]+$")) {
//
//            languageBean.showMeseage("Info", "fromSerialReq");
//            return;
//
//        }
//        if (toSerial != null && !toSerial.trim().isEmpty() && !toSerial.matches("^[0-9A-Fa-f]+$")) {
//
//            languageBean.showMeseage("Info", "toSerialReq");
//            return;
//
//        }
//        if (fromSerial != null && fromSerial.length() >=8)
//        	fromSerial = fromSerial.substring(fromSerial.length()-8);
//        if (toSerial != null && toSerial.length() >=8)
//        	toSerial = toSerial.substring(toSerial.length()-8);
        lsData = new ArrayList<>();
        for (StockTransactionSerial e : getStockDetail().getLsSerial()) {
//            if (StringUtils.compareHexStrings(toSerial, e.getSerialSearch())
//        		&& StringUtils.compareHexStrings(e.getSerialSearch(),fromSerial)) {
//            }
            if(fromSerial!=null&&e.getSerial().toUpperCase().contains(fromSerial.trim().toUpperCase())){
                lsIn.add(e);
            }
        }
        if (lsIn.isEmpty()) {
            lsData = new ArrayList<>();
        } else {
            AnalysisSerial as = new AnalysisSerial(lsIn, null);
            lsData = as.analysis();
        }

    }

    public void reload() {
        lsG = new ArrayList<>();
        lsData = new ArrayList<>();
        setStockDetail(new StockTransactionDetail());
        mapSerrial = new HashMap<>();
        transactionNotificationBean.init();
    }

    public void viewClick() {
        fromSerial = null;
        toSerial = null;
        if(stockDetail!=null && stockDetail.getLsSerial()!=null && !stockDetail.getLsSerial().isEmpty()) {
            AnalysisSerial as = new AnalysisSerial(getStockDetail().getLsSerial(), null);
            lsData = as.analysis();
        }
    }
//    public void viewClick_(SelectEvent event) {
//        stockDetail = (StockTransactionDetail) event.getObject();
//        fromSerial = null;
//        toSerial = null;
//        AnalysisSerial as = new AnalysisSerial(stockDetail.getLsSerial(), null);
//        lsData = as.analysis();
//    }

    /**
     * @return the orderCode
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * @param orderCode the orderCode to set
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * @return the usser
     */
    public String getUsser() {
        return usser;
    }

    /**
     * @param usser the usser to set
     */
    public void setUsser(String usser) {
        this.usser = usser;
    }

    /**
     * @return the lsG
     */
    public List<StockTransactionDetail> getLsG() {
        return lsG;
    }

    /**
     * @param lsG the lsG to set
     */
    public void setLsG(List<StockTransactionDetail> lsG) {
        this.lsG = lsG;
    }

    /**
     * @return the lsData
     */
    public List<SerialA> getLsData() {
        return lsData;
    }

    /**
     * @param lsData the lsData to set
     */
    public void setLsData(List<SerialA> lsData) {
        this.lsData = lsData;
    }

    /**
     * @return the path
     */
    public StreamedContent getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(StreamedContent path) {
        this.path = path;
    }

    /**
     * @return the stockGoods
     */
    /**
     * @return the dendai
     */
    public String getToSerial() {
        return toSerial;
    }

    /**
     * @param toSerial the dendai to set
     */
    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    /**
     * @return the tudai
     */
    public String getFromSerial() {
        return fromSerial;
    }

    /**
     * @param fromSerial the tudai to set
     */
    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
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
	 * @return the transactionNotificationBean
	 */
	public TransactionNotificationBean getTransactionNotificationBean() {
		return transactionNotificationBean;
	}

	/**
	 * @param transactionNotificationBean the transactionNotificationBean to set
	 */
	public void setTransactionNotificationBean(TransactionNotificationBean transactionNotificationBean) {
		this.transactionNotificationBean = transactionNotificationBean;
	}

	/**
     * @return the disableAdd
     */
    public boolean isDisableAdd() {
        disableAdd = true;
        if (lsG != null && !lsG.isEmpty()) {
            disableAdd = false;
        }
        return disableAdd;
    }

    /**
     * @param disableAdd the disableAdd to set
     */
    public void setDisableAdd(boolean disableAdd) {
        this.disableAdd = disableAdd;
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
     * @return the goodsId
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * @param goodsId the goodsId to set
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * @return the listGoods
     */
    public List<EquipmentsProfile> getListGoods() {
        return listGoods;
    }

    /**
     * @param listGoods the listGoods to set
     */
    public void setListGoods(List<EquipmentsProfile> listGoods) {
        this.listGoods = listGoods;
    }

    /**
     * @return the goodsGroup
     */
    public Long getGoodsGroup() {
        return goodsGroup;
    }

    /**
     * @param goodsGroup the goodsGroup to set
     */
    public void setGoodsGroup(Long goodsGroup) {
        this.goodsGroup = goodsGroup;
    }

    /**
     * @return the quantity
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity.trim();
    }

    /**
     * @return the fileDownload
     */
    public StreamedContent getFileDownload() {
        fileDownload = null;
        if (fileDownload == null) {
            if(handleType.equals(InventoryConstanst.ImportType.importGood)){
                InputStream f = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(temp);
                setFileDownload(new DefaultStreamedContent(f, "xlsx", "import_ncc_temp.xlsx"));
            }else if(handleType.equals(InventoryConstanst.ImportType.importKTV)){
                InputStream f = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempktv);
                setFileDownload(new DefaultStreamedContent(f, "xlsx", "import_ktv_temp.xlsx"));
            }else {
                InputStream f = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(tempWarranty);
                setFileDownload(new DefaultStreamedContent(f, "xlsx", "import_mainten_temp.xlsx"));
            }
        }
        return fileDownload;
    }

    /**
     * @param fileDownload the fileDownload to set
     */
    public void setFileDownload(StreamedContent fileDownload) {
        this.fileDownload = fileDownload;
    }

    /**
     * @return the uploadedFile
     */
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    /**
     * @param uploadedFile the uploadedFile to set
     */
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    /**
     * @return the disableUpload
     */
    public boolean isDisableUpload() {
//        disableUpload = false;
        if(providerId==null||goodsId==null||equipType==null||goodsId<1||providerId<1){
            return true;
        }
        if(disableQuantity&&(serialFrom==null || serialFrom.trim().isEmpty())){
            return true;
        }
        if(disableQuantity&&serialFrom.trim().contains(" ")){
            return true;
        }
        if(!quantity.isEmpty()&&!StringUtils.isNumberLong̣̣(quantity.trim())){
            return true;
        }
        if(!waranPrio.isEmpty()&& !StringUtils.isNumberLong̣̣(waranPrio.trim())){
            return  true;
        }
        if(!mainteinPrio.isEmpty()&& !StringUtils.isNumberLong̣̣(mainteinPrio.trim())){
            return  true;
        }
        if (!litecycle.trim().isEmpty() && !StringUtils.isNumberLong̣̣(litecycle.trim()) ){
            return  true;
        }
        if(waranDate!=null&&waranDate.compareTo(new Date())<=0){
            return true;
        }
        if(serialFrom!=null && !serialFrom.isEmpty()){
            String map = serialFrom + "-" + goodsId + "-" + providerId;
            StockTransactionSerial std = mapSerrial.get(map);
            // Check duplicate serial, provider, good_id in file
            if (std != null) {
                return true;
            }
        }
        if(serialFrom != null && !serialFrom.isEmpty() ){
            for (StockTransactionDetail g : getLsG()) {
                for (StockTransactionSerial serial:g.getLsSerial()) {
                    if(serialFrom.equals(serial.getSerial())){
                        return true;
                    }

                }
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if(serialFrom!=null && !serialFrom.isEmpty() && equipmentsDetailService.findBySerial(serialFrom.trim())!=null){
            String s = languageBean.getBundle().getString("importErrorSerial.exit.only");
//                    s = MessageFormat.format(s, i + 1);
            // getLanguageBean().showMeseage_("Info", s);
//                getLanguageBean().showMeseage("error", "errorProcess");
            languageBean.showMeseage("Info", s);
            context.update("frm:messages");
            return  true;
        }
        if(serialFrom!=null && equipmentsDetailService.findBySerialAndProviderIdEquip(serialFrom.trim(), providerId, goodsId)!=null){
            String s = languageBean.getBundle().getString("importErrorSerial.exit");
//                    s = MessageFormat.format(s, i + 1);
            // getLanguageBean().showMeseage_("Info", s);
//                getLanguageBean().showMeseage("error", "errorProcess");
            languageBean.showMeseage("Info", s);
            context.update("frm:messages");
            return  true;
        }
        return disableUpload;
    }

    public boolean isDisableUploadFile() {
        disableUpload = false;
        if(providerId==null||goodsId==null||equipType==null||goodsGroup==null||goodsGroup<1||goodsId<1){
            return true;
        }
        if(quantity.isEmpty()||!StringUtils.isNumberLong̣̣(quantity.trim())){
            return true;
        }
        if(uploadedFile==null){
//            return true;
        }
        return disableUpload;
    }

    public boolean disableBtnWaranty() {
        disableUpload = false;
        if(uploadedFile==null){
//            return true;
        }
        if(goodsId==null||equipType==null || providerId == null || goodsId<1||providerId<1){
            return true;
        }
        if(disableQuantity&&(stockTransactionSerialId==null)){
            return true;
        }
//        if(equipType.equals(1L)&&((serialFrom==null || serialFrom.trim().isEmpty()))){
//            return true;
//        }
        if(!quantity.isEmpty()&&!StringUtils.isNumberLong̣̣(quantity.trim())){
            return true;
        }
//        if(!countCo.isEmpty() && !StringUtils.isNumberLong̣̣(countCo.trim())){
//            return true;
//        }
//        if(!countCq.isEmpty() && !StringUtils.isNumberLong̣̣(countCq.trim())){
//            return true;
//        }
        if(!waranPrio.isEmpty()&& !StringUtils.isNumberLong̣̣(waranPrio.trim())){
            return  true;
        }
        if(!mainteinPrio.isEmpty()&& !StringUtils.isNumberLong̣̣(mainteinPrio.trim())){
            return  true;
        }
        if (!litecycle.trim().isEmpty() && !StringUtils.isNumberLong̣̣(litecycle.trim()) ){
            return  true;
        }
//        if(waranDate!=null&&waranDate.compareTo(new Date())<=0){
//            return true;
//        }
        String map = serialFrom + "-" + goodsId + "-" + providerId;
        StockTransactionSerial std = mapSerrial.get(map);
        // Check duplicate serial, provider, good_id in file
        if (std != null) {
            return true;
        }
        if(serialFrom != null && !serialFrom.isEmpty() ){
            for (StockTransactionDetail g : getLsG()) {
                for (StockTransactionSerial serial:g.getLsSerial()) {
                    if(serialFrom.equals(serial.getSerial())){
                        return true;
                    }

                }
            }
        }
//        List lst = businessService.searchWaran(serialFrom);
//        if(lst!=null && lst.size() > 0){
//            return true;
//        }
        if(changeWaran){
            RequestContext context = RequestContext.getCurrentInstance();
            if(serialNewSingle==null || serialNewSingle.isEmpty()){
                return true;
            }else if(serialNewSingle.trim().contains(" ")){
                return true;
            }

            if(serialNewSingle!=null && equipmentsDetailService.findBySerial(serialNewSingle.trim())!=null){
                String s = languageBean.getBundle().getString("importErrorSerial.exit");
//                    s = MessageFormat.format(s, i + 1);
                // getLanguageBean().showMeseage_("Info", s);
//                getLanguageBean().showMeseage("error", "errorProcess");
                languageBean.showMeseage("Info", s);
                context.update("frm:messages");
                return  true;
            }
        }

        return disableUpload;
    }
    public boolean disableBtnKTV() {

        if(goodsId==null||equipType==null||goodsId<1||providerId<1){
            return true;
        }
        if(disableQuantity&&(equipmentsDetailSerial==null)){
            return true;
        }
        if(!quantity.isEmpty()&&!StringUtils.isNumberLong̣̣(quantity.trim())){
            return true;
        }
        EquipmentsProfile profile = null;
        if(goodsId!=null&&goodsId>0){
            profile = equipmentsProfileService.findByProfileId(goodsId);
        }
        if(profile!=null
                && InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
            if(serialFrom==null || serialFrom.trim().isEmpty()){
                return true;
            }
            if(stockTransactionSerialId==null){
                return true;
            }
        }

        String map = serialFrom + "-" + goodsId + "-" + providerId;
        StockTransactionSerial std = mapSerrial.get(map);
        // Check duplicate serial, provider, good_id in file
        if (std != null) {
            return true;
        }
        if(serialFrom != null && !serialFrom.isEmpty() ){
            for (StockTransactionDetail g : getLsG()) {
                for (StockTransactionSerial serial:g.getLsSerial()) {
                    if(serialFrom.equals(serial.getSerial())){
                        return true;
                    }

                }
            }
        }
        return false;
    }

    public void resetNotSerial(){
        countCo = "";
        countCq ="";
        waranPrio = "";
        waranDate = null;
        mainteinPrio = "";
        litecycle ="";
        stockTransactionSerialId = null;
        serialSingle = "";
    }
    public void resetInput(){

        serialFrom = "";
        quantity  = "1";
        countCo = "";
        countCq ="";
        waranPrio = "";
        waranDate = null;
        mainteinPrio = "";
        litecycle ="";
        stockTransactionSerialId = null;
        serialSingle = "";
//        equipmentDetailId = null;
        transactionActionCode = "";
        transactionActionID = null;
        equipmentsDetailSerial = null;
        providerForm = "";
        providerId = null;
        initCbb();
        if(handleType.equals(InventoryConstanst.ImportType.importWarranty)){
//            equipmentProfileStatus = -1L;
            providerId = -1L;
        }
        if(handleType.equals(InventoryConstanst.ImportType.importKTV)){
            equipmentProfileStatus = -1L;
        }

//        quantity="1";
//        countCo="";
//        countCq="";
//        waranPrio="";
//        mainteinPrio="";
//        litecycle="";
//        waranDate = null;
    }

    /**
     * @param disableUpload the disableUpload to set
     */
    public void setDisableUpload(boolean disableUpload) {
        this.disableUpload = disableUpload;
    }

    /**
     * @return the listReason
     */
    public List<ApDomain> getListReason() {
        return listReason;
    }

    /**
     * @param listReason the listReason to set
     */
    public void setListReason(List<ApDomain> listReason) {
        this.listReason = listReason;
    }

    /**
     * @return the reasonId
     */
    public Long getReasonId() {
        return reasonId;
    }

    /**
     * @param reasonId the reasonId to set
     */
    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    /**
     * @return the goods
     */
    public String getGoods() {
        return goods;
    }

    /**
     * @param goods the goods to set
     */
    public void setGoods(String goods) {
        this.goods = goods;
    }

    /**
     * @return the reasonName
     */
    public String getReasonName() {
        return reasonName;
    }

    /**
     * @param reasonName the reasonName to set
     */
    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    /**
     * @return the lsProvider
     */
    public List<Provider> getLsProvider() {
        return lsProvider;
    }

    /**
     * @param lsProvider the lsProvider to set
     */
    public void setLsProvider(List<Provider> lsProvider) {
        this.lsProvider = lsProvider;
    }

    /**
     * @return the stockDetail
     */
    public StockTransactionDetail getStockDetail() {
        return stockDetail;
    }

    /**
     * @param stockDetail the stockDetail to set
     */
    public void setStockDetail(StockTransactionDetail stockDetail) {
        this.stockDetail = stockDetail;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public Long getUnitType() {
        return unitType;
    }

    public void setUnitType(Long unitType) {
        this.unitType = unitType;
    }

    public Long getEquipType() {
        return equipType;
    }

    public void setEquipType(Long equipType) {
        this.equipType = equipType;
    }

    public List<ApDomain> getLstGroupType() {
        return lstGroupType;
    }

    public void setLstGroupType(List<ApDomain> lstGroupType) {
        this.lstGroupType = lstGroupType;
    }

    public List<EquipmentsGroup> getLsgGroup() {
        return lsgGroup;
    }

    public void setLsgGroup(List<EquipmentsGroup> lsgGroup) {
        this.lsgGroup = lsgGroup;
    }

    public String getCountCo() {
        return countCo;
    }

    public void setCountCo(String countCo) {
        this.countCo = countCo;
    }

    public String getCountCq() {
        return countCq;
    }

    public void setCountCq(String countCq) {
        this.countCq = countCq;
    }

    public String getWaranPrio() {
        return waranPrio;
    }

    public void setWaranPrio(String waranPrio) {
        this.waranPrio = waranPrio;
    }

    public Date getWaranDate() {
        return waranDate;
    }

    public void setWaranDate(Date waranDate) {
        this.waranDate = waranDate;
    }

    public String getMainteinPrio() {
        return mainteinPrio;
    }

    public void setMainteinPrio(String mainteinPrio) {
        this.mainteinPrio = mainteinPrio;
    }

    public String getLitecycle() {
        return litecycle;
    }

    public void setLitecycle(String litecycle) {
        this.litecycle = litecycle;
    }

    public String getSerialFrom() {
        return serialFrom;
    }

    public void setSerialFrom(String serialFrom) {
        this.serialFrom = serialFrom != null ? serialFrom.trim():"";
    }

    public Boolean getDisableQuantity() {
        return disableQuantity;
    }

    public void setDisableQuantity(Boolean disableQuantity) {
        this.disableQuantity = disableQuantity;
    }

    public String getSerialTitle() {
        return serialTitle;
    }

    public void setSerialTitle(String serialTitle) {
        this.serialTitle = serialTitle;
    }

    public Long getEquipmentProfileStatus() {
        return equipmentProfileStatus;
    }

    public void setEquipmentProfileStatus(Long equipmentProfileStatus) {
        this.equipmentProfileStatus = equipmentProfileStatus;
    }

    public String getTransactionActionCode() {
        return transactionActionCode;
    }

    public void setTransactionActionCode(String transactionActionCode) {
        this.transactionActionCode = transactionActionCode;
    }
    public List<String> completeTransactionCode(String query) {
        try {
            List<String> filteredThemes = new ArrayList<String>();
            setTransactionActionCode("");
            filteredThemes.add("");
            for (int i = 0; i < lstTransactionAction.size(); i++) {
                TransactionAction skin = lstTransactionAction.get(i);
                if (skin.getTransactionActionCode().toLowerCase().contains(query.toLowerCase())) {
                    filteredThemes.add(skin.getTransactionActionCode());
                }
                if (skin.getTransactionActionCode().toLowerCase().equals(query.toLowerCase())) {

                    setTransactionActionID(skin.getTransactionActionId());
                }
            }
            return filteredThemes;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public void createTransactionSelect() {
        if (lstTransactionAction != null) {
            if(getTransactionActionCode().equals("")){
                setTransactionActionID(null);
            }else
                for (TransactionAction obj : lstTransactionAction) {
                    if (obj.getTransactionActionCode().equals(getTransactionActionCode())) {
                        setTransactionActionID(obj.getTransactionActionId());
                        break;
                    }
                }
        }
    }

    public Long getTransactionActionID() {
        return transactionActionID;
    }

    public void setTransactionActionID(Long transactionActionID) {
        this.transactionActionID = transactionActionID;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getSerialFrm() {
        return SerialFrm;
    }

    public void setSerialFrm(String serialFrm) {
        SerialFrm = serialFrm;
    }
    public Long getHandleType() {
        return handleType;
    }

    public void setHandleType(Long handleType) {
        this.handleType = handleType;
        lsg = sessionBean.getLsEquipments();
        if(handleType.equals(InventoryConstanst.ImportType.importKTV)){
            transactionType = InventoryConstanst.TransactionType.IM_STAFF;
            stockStatusSerial = InventoryConstanst.StockStatus.EX_USED;
        }else {
            if(handleType.equals(InventoryConstanst.ImportType.importGood)){
                transactionType = InventoryConstanst.TransactionType.IM;
                stockStatusSerial = InventoryConstanst.StockStatus.INSTOCK;
            }else {
                transactionType = InventoryConstanst.TransactionType.IM_WARRANTY;
                stockStatusSerial = InventoryConstanst.StockStatus.EX_WARANTIED;
            }
        }
        lstSerialStockGoods = businessService.getSerialStockGoodsImportKTV(sessionBean.getShop().getShopId(), goodsId,
                null, stockStatusSerial);
        initStockStatusSerial();
        initPXK();

        shopSingleName = sessionBean.getShop().getShopCode() +" | "+ sessionBean.getShop().getShopName();
        SimpleDateFormat sf = new SimpleDateFormat(".yy.MM.dd.");
        initOrderCode();
    }

    public EquipmentsDetail getEquipmentsDetailSerial() {
        return equipmentsDetailSerial;
    }

    public void setEquipmentsDetailSerial(EquipmentsDetail equipmentsDetailSerial) {
        this.equipmentsDetailSerial = equipmentsDetailSerial;
    }

    public List<StockGoodsInvSerialDTO> getLstSerialStockGoods() {
        return lstSerialStockGoods;
    }

    public void setLstSerialStockGoods(List<StockGoodsInvSerialDTO> lstSerialStockGoods) {
        this.lstSerialStockGoods = lstSerialStockGoods;
    }

    public Long getStockTransactionSerialId() {
        return stockTransactionSerialId;
    }

    public void setStockTransactionSerialId(Long stockTransactionSerialId) {
        this.stockTransactionSerialId = stockTransactionSerialId;
    }
    public void handleUploadComplete(){
        disableUploadNCC = false;
    }

    public boolean isDisableUploadNCC() {
        return disableUploadNCC;
    }

    public void setDisableUploadNCC(boolean disableUploadNCC) {
        this.disableUploadNCC = disableUploadNCC;
    }

    public Boolean getChangeWaran() {
        return changeWaran;
    }

    public void setChangeWaran(Boolean changeWaran) {
        this.changeWaran = changeWaran;
    }

    public String getShopSingleName() {
        return shopSingleName;
    }

    public void setShopSingleName(String shopSingleName) {
        this.shopSingleName = shopSingleName;
    }

    public String getSerialNewSingle() {
        return serialNewSingle;
    }

    public void setSerialNewSingle(String serialNewSingle) {
        this.serialNewSingle = serialNewSingle;
    }

    public String getTitleSerilNew() {
        return titleSerilNew;
    }

    public void setTitleSerilNew(String titleSerilNew) {
        this.titleSerilNew = titleSerilNew;
    }

    public String getSerialSingle() {
        return serialSingle;
    }

    public void setSerialSingle(String serialSingle) {
        this.serialSingle = serialSingle;
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

    public List<Boolean> getVisibleTable2() {
        return visibleTable2;
    }

    public void setVisibleTable2(List<Boolean> visibleTable2) {
        this.visibleTable2 = visibleTable2;
    }

    public void onToggleActiveEtag2(ToggleEvent e) {
        visibleTable2.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);

    }

    public String getProviderForm() {
        return providerForm;
    }

    public void setProviderForm(String providerForm) {
        this.providerForm = providerForm;
    }
}
