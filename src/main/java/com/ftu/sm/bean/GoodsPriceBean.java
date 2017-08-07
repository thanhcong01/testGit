package com.ftu.sm.bean;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;

import com.ftu.admin.consumer.AuthorizationConsumer;
import com.ftu.admin.consumer.entity.IdentityEntity;
import com.ftu.admin.consumer.entity.IdentityOutput;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.inventory.bean.GoodsAllBean;
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.java.business.EquipmentsProfileService;
import com.ftu.language.LanguageBean;
import com.ftu.sm.bo.GoodsPrice;
import com.ftu.sm.service.GoodsPriceService;
import com.ftu.sm.service.imp.GoodsPriceServiceImp;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bussiness.AppDomainService;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
@ViewScoped
public class GoodsPriceBean implements Serializable {
	private EquipmentsProfileService service = new EquipmentsProfileService();
	private GoodsPriceService priceService = new GoodsPriceServiceImp();
	private AppDomainService domainService = new AppDomainService();
	public String TYPE_EDIT = "edit";
	public String TYPE_ADD = "add";
	public String TYPE_SEARCH = "search";
	private String type;
	private List<GoodsPrice> listGoodsPrice;
	private GoodsPrice priceSelected = new GoodsPrice();
	@ManagedProperty("#{languageBean}")
	private LanguageBean languageBean;
	@ManagedProperty("#{goodsAllBean}")
	private GoodsAllBean bean;
	private List<ApDomain> listDomains;
	private List<EquipmentsProfile> listGoods;
	private EquipmentsProfile goods = new EquipmentsProfile();
	private List<GoodsPrice> listPriceSelected;

	@PostConstruct
	public void init() {
		loadAll();
	}
	
	public List<GoodsPrice> getListPriceSelected() {
		return listPriceSelected;
	}
	
	public void setListPriceSelected(List<GoodsPrice> listPriceSelected) {
		this.listPriceSelected = listPriceSelected;
	}

	public EquipmentsProfile getGoods() {
		return goods;
	}

	public void setGoods(EquipmentsProfile goods) {
		this.goods = goods;
	}

	public List<EquipmentsProfile> getListGoods() {
		if (listGoods == null) {
			listGoods = service.getAllProfile(null, false, -1, -1);
		}
		return listGoods;
	}

	public void setListGoods(List<EquipmentsProfile> listGoods) {
		this.listGoods = listGoods;
	}

	public List<ApDomain> getListDomains() {
		return listDomains;
	}

	public void setListDomains(List<ApDomain> listDomains) {
		this.listDomains = listDomains;
	}

	public GoodsAllBean getBean() {
		return bean;
	}

	public void setBean(GoodsAllBean bean) {
		this.bean = bean;
	}

	public GoodsPrice getPriceSelected() {
		return priceSelected;
	}

	public void setPriceSelected(GoodsPrice priceSelected) {
		this.priceSelected = priceSelected;
	}

	public List<GoodsPrice> getListGoodsPrice() {
		return listGoodsPrice;
	}

	public void setListGoodsPrice(List<GoodsPrice> listGoodsPrice) {
		this.listGoodsPrice = listGoodsPrice;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTYPE_ADD() {
		return TYPE_ADD;
	}

	public void setTYPE_ADD(String tYPE_ADD) {
		TYPE_ADD = tYPE_ADD;
	}

	public String getTYPE_EDIT() {
		return TYPE_EDIT;
	}

	public void setTYPE_EDIT(String tYPE_EDIT) {
		TYPE_EDIT = tYPE_EDIT;
	}

	public String getTYPE_SEARCH() {
		return TYPE_SEARCH;
	}

	public void setTYPE_SEARCH(String tYPE_SEARCH) {
		TYPE_SEARCH = tYPE_SEARCH;
	}

	public LanguageBean getLanguageBean() {
		return languageBean;
	}

	public void setLanguageBean(LanguageBean languageBean) {
		this.languageBean = languageBean;
	}
	
	public void onRowSelected(){
		
	}

	private void loadDomain() {
		ApDomain domain = new ApDomain();
		domain.setType("PRICE_TYPE");
		listDomains = domainService.getAllDomains(domain, false);
	}

	public List<String> autoCompleteUsername(String query) {
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData sessionData = (SessionData) session.getAttribute("username");
			IdentityOutput output = AuthorizationConsumer.getAllIdentities(sessionData.getTransEntity());
			List<IdentityEntity> list = output.getIdentities();

			List<String> filteredThemes = new ArrayList<String>();

			for (int i = 0; i < list.size(); i++) {
				IdentityEntity skin = list.get(i);
				if (skin.getUsername().toLowerCase().contains(query.toLowerCase())) {
					filteredThemes.add(skin.getUsername());
				}
			}
			return filteredThemes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<EquipmentsProfile> autoCompleteGoods(String str) {

		List<EquipmentsProfile> filteredThemes = new ArrayList<EquipmentsProfile>();
		try {
			for (int i = 0; i < getListGoods().size(); i++) {
				EquipmentsProfile skin = getListGoods().get(i);
				if (skin.getProfileName().toUpperCase().contains(str.toUpperCase().trim())) {
					filteredThemes.add(skin);
				}
			}
			return filteredThemes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filteredThemes;
	}

	public String commaSeparateNumber(String val) {
		try {
			double valD = new Double(val);
			return NumberFormat.getInstance().format(valD);
		} catch (Exception ex) {

		}
		return null;
	}
	
	public String getPriceType(String code){
		for(ApDomain ap:listDomains){
			if(ap.getCode().equals(code)){
				return ap.getName();
			}
		}
		return null;
	}
	
	

	public void loadGoodsPrice(boolean search) {
		TreeNode node = bean.getSelectedNode();
		if (node != null) {
			Object object = node.getData();
			if (object != null) {
				if (object instanceof EquipmentsProfile) {
					EquipmentsProfile tmp = (EquipmentsProfile) object;
					GoodsPrice g = new GoodsPrice();
					g.setGoodsId(tmp.getProfileId());
					if (!search) {
						g.setStatus("1");
					}
					listGoodsPrice = priceService.getAllGoods(g, false, -1, -1);
				}
			}
		}

	}

	public void loadAll() {
		loadGoodsPrice(false);
		loadDomain();
	}

	public void onRowSelect() {

	}

	public void save() {
		FacesMessage message = null;
		try {
			if (type.equals(TYPE_ADD)) {
				priceSelected.setStatus("1");
				priceSelected.setGoodsId(goods.getProfileId());
			}
			priceService.saveOrUpdate(priceSelected);
			loadGoodsPrice(false);
			if (type.equals(TYPE_ADD)) {
				reset();
			}
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
			languageBean.getMessage("common.action.save.success",null,false));
		} catch (Exception ex) {
			ex.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getMessage("common.confirm.header", null, false), 
			languageBean.getMessage("common.action.save.error",null,false));
		}
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

	public void delete() {
		FacesMessage message = null;
		try {
			for(GoodsPrice gp :listPriceSelected){
				gp.setStatus("0");
				priceService.saveOrUpdate(gp);
			}
			loadGoodsPrice(false);
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
			languageBean.getMessage("common.action.delete.success", null, false));
		} catch (Exception e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, languageBean.getMessage("common.confirm.header", null, false), 
			languageBean.getMessage("common.action.delete.error", null, false));
		} finally {

		}
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

	public void filterSearch() {
		priceSelected.setGoodsId(goods.getProfileId());
		listGoodsPrice = priceService.getAllGoods(priceSelected, true, -1, -1);
	}

	public void beforeDelete() {

		FacesMessage message = null;
		if (listPriceSelected == null || listPriceSelected.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
			languageBean.getMessage("common.action.beforedelete", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("document.getElementById('tv:frmGP:confirmButtonGP').click(); ");
		}
	}

	public void beforeSave() {

		FacesMessage message = null;
		if (priceSelected == null || listPriceSelected.isEmpty()) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, languageBean.getMessage("common.confirm.header", null, false), 
			languageBean.getMessage("common.acction.beforesave", null, false));
			RequestContext context = RequestContext.getCurrentInstance();
			context.showMessageInDialog(message);
		} else {
			if(listPriceSelected.size() > 1){
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, languageBean.getMessage("common.confirm.header", null, false), 
				languageBean.getMessage("common.action.beforesave.multiplerecord", null, false));
				RequestContext context = RequestContext.getCurrentInstance();
				context.showMessageInDialog(message);
			}else{
				priceSelected = listPriceSelected.get(0);
				goods.setProfileId(priceSelected.getGoodsId());
				goods.setProfileName(priceSelected.getGoodsName());
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('dlSaveGP').show();");
			}
			
		}
	}

	public GoodsPrice reset() {
		priceSelected = new GoodsPrice();
		// goods = new Goods();
		return priceSelected;
	}

}
