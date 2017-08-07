/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.java.bo.LazyEquipmentsModel;
import com.ftu.java.bo.LazyGoodsModel;
import com.ftu.language.LanguageBean;
import com.ftu.inventory.bo.StockGoodsInvSerial;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.utils.ComponentUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author E5420
 */
@ManagedBean
@SessionScoped
public class SearchEtag implements Serializable {

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;
    @ManagedProperty(value = "#{languageBean}")
    private LanguageBean languageBean;
    private List<Shop> listShop;
    private Long shopId;
    private List<EquipmentsProfile> listGoods;
    private Long goodsId;
    private Long goodsStatusId;
    private List<ApDomain> listStockStatus;
    private Long stockStatusId;
    private String fromSerial;
    private String toSerial;
    private LazyDataModel<StockGoodsInvSerialDTO> lsData;
    private List<Provider> listProvider;
    private Long providerId;
    private StockGoodsInvSerial etag = new StockGoodsInvSerial();
    private EtagDetail etadDetail = new EtagDetail();
    private List<TransactionAction> listTrans = new ArrayList<>();
    private List<GoodsStatusTransSerial> listStatusTrans = new ArrayList<>();
    List<ApDomain> listTransStatus;
    List<ApDomain> listTransType;
    private Long goodsGroupId;
    List<EquipmentsGroup> lsGoodsGroup;
    boolean fist = true;
    private String goods;
    
    //huy
    private  String createShop;

    @PostConstruct
    public void init() {
    	sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
        listStockStatus = sessionBean.getService().getListSearchStocktatus();
        listShop = new ArrayList<>();
        Shop allShop = new Shop(-1l);
        allShop.setShopName("Tất cả");
        listShop.add(allShop);
        _getListShop(sessionBean.getService().getAllShop(shopId));
        listProvider = sessionBean.getLsProvider();
        listTransStatus = sessionBean.getService().getListTransStatus();
        listTransType = sessionBean.getService().getListTransType();
        lsGoodsGroup = sessionBean.getLsgGroup();
        changeGoodsGroup();
        search();
    }
    
    public void CreateShopSelect() {
		if (listShop != null) {
			for (Shop shop : listShop) {
				if (shop.getShopName().equals(createShop)) {
					shopId = shop.getShopId();
					break;
				}
			}
		}
	}

	public List<String> CompleteCreateShop(String query) {
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData data = (SessionData) session.getAttribute("username");
			List<String> filteredThemes = new ArrayList<String>();

			for (int i = 0; i < listShop.size(); i++) {
				Shop skin = listShop.get(i);
				if (skin.getShopName().toLowerCase().contains(query.toLowerCase())) {
					filteredThemes.add(skin.getShopName());
				}
				if (skin.getShopName().toLowerCase().equals(query.toLowerCase())) {
					shopId = skin.getShopId();
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
    	if (listShop.size() == 0)
    	{
    		Shop allShop = new Shop(-1l);
            allShop.setShopName("Tất cả");
            listShop.add(0,allShop);
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

    public void changeGoodsGroup() {
        if (lsGoodsGroup != null && !lsGoodsGroup.isEmpty() && (goodsGroupId == null || goodsGroupId == 0L)) {
            setGoodsGroupId(lsGoodsGroup.get(0).getEquipmentsGroupId());
        }
        if (getGoodsGroupId() != null && getGoodsGroupId() > 0) {
            List<EquipmentsProfile> lsg = sessionBean.getLsEquipments();
            listGoods = new ArrayList<>();
            if (lsg != null && !lsg.isEmpty()) {
                for (EquipmentsProfile g : lsg) {
                    if (goodsGroupId.equals(g.getEquipmentsGroupId())) {
                        listGoods.add(g);
                    }
                }
            }
        }

        goodsId = 0L;
        goods = "";

    }

    public void goodsSelect() {
        if (listGoods != null) {
            for (EquipmentsProfile g : listGoods) {
                if ((g.getProfileCode() + "-" + g.getProfileName()).equals(goods)) {
                    goodsId = g.getProfileId();
                    break;
                }
            }
        }
    }

    public List<String> completeGoods(String gs) {
        goods = gs;
        List<String> rs = new ArrayList<>();
        if (listGoods != null && !listGoods.isEmpty()) {
            for (EquipmentsProfile g : listGoods) {
                if (gs == null || gs.isEmpty() || g.getProfileCode().toLowerCase().contains(goods.toLowerCase())) {
                    rs.add(g.getProfileCode() + "-" + g.getProfileName());
                }
            }
        }
        return rs;
    }

    private String getShopName(Long shopId) {
        if (listShop != null) {
            for (Shop d : listShop) {
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
        setEtag((StockGoodsInvSerial) event.getObject());
        listTrans = sessionBean.getService().getTaBySerial(getEtag().getProviderId(), getEtag().getEquipmentProfileId(), getEtag().getSerial());
        if (listTrans != null) {
            for (TransactionAction ta : listTrans) {
                ta.setShopName(getShopName(ta.getFromShopId()));
                ta.setToShop(getShopName(ta.getToShopId()));
                ta.setTypeName(getTransType(Long.parseLong(ta.getTransactionType())));
                ta.setReasonName(sessionBean.getService().getReasonByValue(ta.getReasonId()).getName());
                ta.setStatusName(getTransStatus(Long.parseLong(ta.getStatus())));
            }
        }
        listStatusTrans = sessionBean.getService().getTransSerial(getEtag().getProviderId(), getEtag().getEquipmentProfileId(), getEtag().getSerial());
        if (listStatusTrans != null) {
            for (GoodsStatusTransSerial gs : listStatusTrans) {
                GoodsStatusTrans gsTrans = sessionBean.getService().getGoodsStatusTrans(gs.getGoodsStatusTransId());
                gsTrans.setShopName(getShopName(gsTrans.getShopId()));
                
                gs.setGoodsTrans(gsTrans);
                List<ApDomain> lsReason = sessionBean.getService().getListEvaluaReason(null);
                for (ApDomain d : lsReason) {
                    if (d.getValue().equals(gsTrans.getReasonId())) {
                        if (d.getCode().equals(InventoryConstanst.GoodsTransType.GOODS_STATUS.toString())) {
                            gs.setNewStatus(sessionBean.getService().getEquipsStatusName(gs.getNewGoodsStatus()));
                            gs.setOldStatus(sessionBean.getService().getEquipsStatusName(gs.getOldGoodsStatus()));
                            gsTrans.setReason(d.getName());

                        } else {
                            gs.setNewStatus(sessionBean.getStockStatusName(gs.getNewGoodsStatus().toString()));
                            gs.setOldStatus(sessionBean.getStockStatusName(gs.getOldGoodsStatus().toString()));
                            gsTrans.setReason(d.getName());

                        }
                        break;
                    }
                }
            }
            sort();
        }
//        if (listTrans != null && !listTrans.isEmpty()) {
//            etadDetail = sessionBean.getService().getByEtagSerial(listTrans.get(0).getTransactionActionId(), etag.getSerial());
//        }
        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler nh = fc.getApplication().getNavigationHandler();
        nh.handleNavigation(fc, null, "/search-etag-sub.xhtml?faces-redirect=false");

    }

    public void search() {
     if (fromSerial != null && !fromSerial.trim().isEmpty() && !fromSerial.matches("^[0-9A-Fa-f]+$")) {

            languageBean.showMeseage("Info", "fromSerialReq");
            return;

        }
        if (toSerial != null && !toSerial.trim().isEmpty() &&!toSerial.matches("^[0-9A-Fa-f]+$")) {
         
                languageBean.showMeseage("Info", "toSerialReq");
            return;

        }
        if (!fist) {
            ComponentUtils cu = new ComponentUtils();
            DataTable dt = (DataTable) cu.findComponent("dtSearchEtagSerial");
            if (dt != null) {
				dt.setFirst(0);
			}
        }
        if((createShop ==  null) || createShop.equals("")){
        	shopId = null;
        }
        
        fist = false;
//        if (shopId == null && !listShop.isEmpty()) {
//            shopId = listShop.get(0).getShopId();
//        }
        StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO("", goodsStatusId, stockStatusId == null ? null : stockStatusId.toString(), shopId == null ? null : shopId, providerId, goodsId);
        lsData = new LazyGoodsModel(fromSerial, getToSerial(), sgis, goodsGroupId, listShop);
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
     * @return the goodsStatusId
     */
    public Long getGoodsStatusId() {
        return goodsStatusId;
    }

    /**
     * @param goodsStatusId the goodsStatusId to set
     */
    public void setGoodsStatusId(Long goodsStatusId) {
        this.goodsStatusId = goodsStatusId;
    }

    /**
     * @return the listStockStatus
     */
    public List<ApDomain> getListStockStatus() {
        return listStockStatus;
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
    public LazyDataModel<StockGoodsInvSerialDTO> getLsData() {
        return lsData;
    }

    /**
     * @param lsData the lsData to set
     */
    public void setLsData(LazyDataModel<StockGoodsInvSerialDTO> lsData) {
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
     * @return the goodsGroupId
     */
    public Long getGoodsGroupId() {
        return goodsGroupId;
    }

    /**
     * @param goodsGroupId the goodsGroupId to set
     */
    public void setGoodsGroupId(Long goodsGroupId) {
        this.goodsGroupId = goodsGroupId;
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
    
}
