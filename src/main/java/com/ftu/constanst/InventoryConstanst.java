/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.constanst;

public class InventoryConstanst {

    public static final String local = "vi";

    public static class TransactionStatus {

        //Tạo
        public static String CREATE = "1";
        //Duyệt
        public static String APPROVE = "2";
        //Hủy
        public static String CANCEL = "3";
        //Xuất
        public static String EX = "4";
        //Nhập
        public static String IM = "5";

        //Tạo ngang cap
        public static String CREATE_R = "6";
        //Duyệt ngang cap
        public static String APPROVE_R = "7";
        //Hủy ngang cap
        public static String CANCEL_R = "8";
        //Xuất ngang cap
        public static String EX_R = "9";
        //Nhập ngang cap
        public static String IM_R = "10";

        //Kho KTV- Staff
        public static String CREATE_STAFF = "11";
        //Duyệt KTV
        //public static String APPROVE_S = "12";
        //Hủy KTV
        //public static String CANCEL_STAFF = "13";
        //Xuất KTV
        public static String EX_STAFF = "14";
        //Nhập KTV
        public static String IM_STAFF = "15";

        // tao bh, sửa chữa
        public static String CREATE_WARRANTY = "16";
        //Duyệt bh, sửa chữa
//        public static String APPROVE_WARAN = "17";
        //Hủy bh, sửa chữa
        public static String CANCEL_WARRANTY = "18";
        //Xuất bh, sửa chữa
        public static String EX_WARRANTY = "19";
        //Duyet bh, sửa chữa
        public static String IM_WARRANTY = "20";

    }

    public static class TransactionTypeCompact{
        //Xuất kho
        public static String EX = "1";
        //Nhập kho
        public static String IM = "2";
    }

    public static class TransactionType {
        //Nhập hàng
        public static String IM = "10";
      //Nhập KTV
        public static String IM_STAFF = "11";
      //Nhập bh sửa chữa
        public static String IM_WARRANTY = "12";
        
        //Điều chuyển trên dưới
        public static String INSTANT_TD = "20";
        //Điều chuyển  ngang cấp
        public static String INSTANT_NC = "25";
        
        //Xuất trả hàng mới
        public static String EX_PARENT = "30";
        //Xuất trả hàng hỏng
        public static String EX_PARENT_ERR = "31";
        //Xuất trả hàng đã sử dụng
        public static String EX_PARENT_USED = "32";
        
        //Xuất hàng ra khỏi kho
        //public static String EX = "40";
        //Xuất hàng KTV
        public static String EX_STAFF = "41";
        //Xuất bảo hành
        public static String EX_WARANTY = "42";
        //Xuất sửa chữa
        public static String EX_REPAIR = "43";
        
        //Thanh hủy hàng hỏng
//        public static String DEL = "7";

//        public static String RANGE = "9"; //nhap hang ngang cap;

//        public static String AGENCY = "10"; //nhap kho dai ly

//        public static String KTV_CREATE = "19";//Nhap hang tu ky thuat vien
//        public static String IM_STAFF = "15";//Nhap hang tu ky thuat vien
//        public static String EX_STAFF = "14";//Xuat hang tu ky thuat vien
//
//        public static String IM_WARRANTY = "20";//Nhap hang tu ky thuat vien
//        public static String EX_WARRANTY = "19";//Xuat hang tu ky thuat vien

//        public static String EX_SATFF = "19";//Nhap hang tu ky thuat vien
//        public static String IM_SATFF = "20";//Nhap hang tu ky thuat vien
    }
    public static class ImportType{
        public static Long importGood = 1L;
        public static Long importWarranty = 0L;
        public static Long importKTV = 19L;
    }

    public static class StockTransactionType {
        //Nhập từ nhà CC
        public static String IM = "1";
        //Điều chuyển
        public static String TRANS = "2";
        //Xuất ra khoi kho
        public static String EX = "3";
        //Trả nhà CC
        public static String BACK = "4";
        //Thanh hủy hàng hỏng
        //public static String DEL = "5";

        //Nhập từ KTV
        public static String IM_STAFF = "10";
        //Điều chuyển KTV
        //public static String TRANS_STAFF = "12";
        //Xuất KTV
        public static String EX_STAFF = "13";
        //Trả KTV
        //public static String BACK_STAFF = "14";
        //Thanh hủy hàng hỏng
        //public static String DEL = "5";

        //Nhập bh
        public static String IM_WARRANTY = "20";
        //Điều chuyển
        //public static String TRANS_WARRANTY = "22";
        //Xuất bh
        public static String EX_WARRANTY = "23";
        //Trả nhà CC
        //public static String BACK_WARRANTY = "24";
        //Thanh hủy hàng hỏng
        //public static String DEL_WARRANTY = "25";
    }

    public static class StockTransactionStatus {

        //Từ chối
        public static String REJECT = "0";
        //Hoàn thành
        public static String SUCCESS = "1";
        //Chờ nhận
        public static String WAIT = "2";
    }

    public static class StockTransactionDeliveryType {

        //Chuyển ngay
        public static String TRANS = "1";
        //Chờ nhận
        public static String WAIT = "2";
    }

    public static class StockStatus {
        //Đang kiểm duyệt

        public static final String WAIT = "0";
        //Sẵn có
        public static final String INSTOCK = "1";
        //Đã bán
        public static final String SOLD = "3";
        //Đang xuất trả nhà CC
        public static final String WAIT_EX = "4";
        //Đã xuất trả nhà CC
        public static final String EX = "5";
        //Hủy
        public static final String CANCEL = "6";
        //Thiếu mất (trong kho)
        public static final String INSTOCK_ERR = "7";
        //Đã xuất KTV(Khi nhap chi chon nhưng serial co kho status = 8)
        public static final String EX_USED = "8";
        //Da lap dat
        public static final String USED = "9";
        //Dang xuat bao hanh, sua chua
        public static final String EX_WARANTY = "10";
        //Đã xuat bao hanh, sua chua
        public static final String EX_WARANTIED = "11";
        //Hàng đã đổi trả
        public static final String CHANGE_WARANTIED = "12";
        //BLOCK, dang dieu chuyen Tren duoi
//        public static final String BLOCK = 2
        public static final String BLOCK_TD = "15";
        //BLOCK, dang dieu chuyen, Ngang cap
        public static final String BLOCK_NC = "16";
        //BLOCK, dang dieu chuyen, Xuat tra
        public static final String BLOCK_XT = "17";
    }

    public static class GoodsStatus {

        //HỎNG
        public static Long ERROR = 2L;
        //Bình thường
        public static Long NOMAL = 1L;
        //Đã sử dụng
        public static Long USED = 3L;
    }

    public static class GoodsTransType {

        public static Long GOODS_STATUS = 1L;

        public static Long STOCK_STATUS = 2L;
    }

    public static class ApDomainType {

        public static String GOODS_STATUS = "GOODS_STATUS";//"GOODS_STATUS";
        public static String STOCK_STATUS = "STOCK_STATUS";
        public static String TRANS_TYPE = "TRANS_TYPE";
        public static String TRANS_STATUS = "TRANS_STS";
        public static String TRANS_REASON = "TRANS_REASON";
        public static String GOODS_TRANS_TYPE = "GOODS_TRANS_TYPE";
        public static String GOODS_TRANS_REASON = "GOODS_TRANS_REASON";
        public static String ACTION_TYPE = "ACTION_TYPE";
        public static String EQUIP_STATUS = "EQUIP_STATUS";
        public static String EQUIP_TYPE = "EQUIP_TYPE";
        public static String WARAN_TATUS = "WARAN_STATUS";
        public static String MANTENANCE_TYPE = "MANTENANCE_TYPE";//MANAGEMENT_TYPE
        public static String POSITION_TYPE = "POSITION_TYPE";
        public static String UNIT_TYPE = "UNIT_TYPE";
        public static String ORIGIN_TYPE = "ORIGIN_TYPE";
        public static String MANUFACTURE_TYPE = "MANUFACTURE_TYPE";
        public static String STAFF_TYPE = "STAFF_TYPE";
        public static String FIGURE_NAME_TYPE = "FIGURE_NAME_TYPE";
        public static String ACTION_AU_DETAIL = "ACTION_AU_DETAIL";

    }

    public static Long EQUIP_TYPE_1L = 1L;
    public static Long EQUIP_TYPE_2l = 2L;

    public static class MANAGEMENT_TYPE {
        public static Long MANTENANCE_TYPE_1L = 1L;
        public static Long MANTENANCE_TYPE_2L = 2L;
        public static Long MANTENANCE_TYPE_3L = 3L;
    }
    public static class ACTION_TYPE {
        //Lắp đặt mới
        public static Long TYPE_1L = 1L;
        //Bao trì
        public static Long TYPE_2L = 2L;
        //sua chua/sự cố
        public static Long TYPE_3L = 3L;
        //Thay đổi thông tin
        public static Long TYPE_4L = 4L;
        //Diều chỉnh
        public static Long TYPE_5L = 5L;
        //Nhap hang tu nha cung cap
        public static Long TYPE_6L = 6L;
        //Dieu chuyen tren dưới
        public static Long TYPE_7L = 7L;
        //Dieu chuyen ngang cap
        public static Long TYPE_8L = 8L;
        //Xuat tra cap tren
        public static Long TYPE_9L = 9L;
        //Xuat KTV
        public static Long TYPE_10L = 10L;
        //Nhap KTV
        public static Long TYPE_11L = 11L;
        //Xuat BH, Sua chua
        public static Long TYPE_12L = 12L;
        //Nhap BH, Sua chua
        public static Long TYPE_13L = 13L;
    }
    public static class WARAN_STATUS{
        public static Long WARAN_STATUS_1L = 1L;//Đã bảo hành
        public static Long WARAN_STATUS_2L = 2L;//Chưa bảo hành
    }

    public static class RoleKey {

        public static String VIEW = "VIEW";
        public static String ACCESS = "ACCESS";
        public static String INSERT = "INSERT";
        public static String UPDATE = "UPDATE";
        public static String DELETE = "DELETE";
        public static String IMPORT = "IMPORT";
        public static String EXPORT = "EXPORT";
        public static String APPROVE = "APPROVE";
        public static String REJECT = "REJECT";
    }

    public static class ProviderStatus {

        public static Long ACTIVE = 1L;
        public static Long IN_ACTIVE = 0L;
    }
    public static class TransactionNotification
    {
        public static String imPshopExGoods = "im-pshop-ex-goods.xhtml";
        public static String imSubShopImGoods = "im-subshop-im-goods.xhtml";
        public static String exPShopApprove = "ex-pshop-approve.xhtml";
        public static String exSubShopExGoods = "ex-subshop-ex-goods.xhtml";
        public static String exPshopImGoods = "ex-pshop-im-goods.xhtml";
        public static String searchTransaction = "search-transaction.xhtml";
    }
    public static class TransactionActionCode {
        //xuat ky thuat vien
        public static String EMEX = "EMEX";
        //Nhap hang tu nha cung cap, bh
        public static String IM =  "IM";
        public static String EX =  "EX";
    }
    public static class ACTION_AU_DETAIL{
        public static String	POSITION_ID ="1";//	Vị trí	1
        public static String	EQUIPMENT_STATUS = "2";//	Trạng thái TB	2
        public static String	REFERENCE_ID = "3";//	Mã tham chiếu	3
        public static String	SPECIFICATION = "4";//	Thông số kỹ thuật	4
        public static String	MAINTANCE_PERIOD = "5";//	Chu kỳ bảo trì	5
        public static String	LIFE_CYCLE = "6";//	Vòng đời	6
        public static String	AVAILABLE_QUANTITY = "7";//	Số lượng trong kho	7
        public static String	REFERENCE_ID_DC = "8";//	Mã phê duyệt	8
        public static String	DOCUMENT = "9";//	Linh tài liệu	9
        public static String	NOTE = "11";//	Ghi chú	11
        public static String	SERIAL ="10";//	Serial	10
        public static String	STOCK_STATUS ="12";//	Trạng thái kho
        public static String	SHOP_ID ="13";//	Kho
        public static String	PROVIDER_ID ="14";//	NCC
        public static String	STAFF_ID ="15";//	Nhan vien nhan(Xuat KTV, xuat bao duong)
        public static String	SERIAL_APPRO ="16";//	Serial	10
        public static String	QUANTITY = "17"; //Số lượng nhập
        public static String	QUANTITY_DC = "18"; //Số lượng điều chuyển
        public static String	QUANTITY_XT = "19"; //Số lượng xuất trả
        public static String	QUANTITY_KTV = "20"; //Số lượng xuất trả
    }

    public static class REASON_CODE{
        //Loai ly do cho Dieu chinh
        public static Long evaluate = 3L;
    }

    public static class PRIFIX_TRANTYPE{
        public static String IMPORT_NCC = "IM_" + TransactionType.IM;
        public static String IMPORT_KTV = "IM_" + TransactionType.IM_STAFF;
        public static String IMPORT_WARRAN = "IM_" + TransactionType.IM_WARRANTY;
    }
    public static class RESOURCE_LOG{
        public static String LOG_SHOP = "EQUIPMENT.DEPOT";
        public static String LOG_START = "EQUIPMENT.EMP";
        public static String LOG_PROVIDER = "EQUIPMENT.PROVIDER";
        public static String LOG_GROUP_EQUIP = "EQUIPMENT.G.DEVICE";
        public static String LOG_EQUIP = "EQUIPMENT.DEVICE" ;
        public static String LOG_POSITION = "EQUIPMENT.POSITION";
        public static String LOG_APDOIMAIN = "EQUIPMENT.KPI";
    }
    public static class APDOMAIN_TYPE_NOT_DELETE{
        public static String[] TYPE_PROFILE = {"MANUFACTURE_TYPE", "ORIGIN_TYPE"};
        public static String[] TYPE_POSITION = {"FIGURE_NAME_TYPE", "POSITION_TYPE"};
    }
}
