package com.ocr.system.model;
/**
 * 存折
 * @author Jocker
 * @date 2021/3/1
 */
public class CunZheBack {
    /**
     * 流水ID
     */
    private String tradeId;
    /**
     * 户名
     */
    private String  ACName;
    /**
     *账号
     */
    private String ACNo;
    /**
     *产品种类
     */
    private String ChanPinZhongLei;
    /**
     *柜面支取方式
     */
    private String GuiMianZhiQuFangShi;
    /**
     *开户银行
     */
    private String KaiHuYinHang;
    /**
     *银行联系电话
     */
    private String YinHangLianXiDianHua;
    /**
     *存折册号
     */
    private String CunZheCeHao;
    /**
     *通存通兑方式
     */
    private String TongCunTongDuiFangShi;
    /**
     *支付系统行号
     */
    private String ZhiFuXiTongHangHao;
    /**
     *经办柜员
     */
    private String JingBanGuiYuan;
    /**
     *注册日期
     */
    private String IssueDate;
    /**
     *开户日期
     */
    private String KaiHuDate;
    /**
     *存折号
     */
    private String PassbookNo;

    public CunZheBack() {
    }

    public String getACName() {
        return ACName;
    }

    public void setACName(String ACName) {
        this.ACName = ACName;
    }

    public String getACNo() {
        return ACNo;
    }

    public void setACNo(String ACNo) {
        this.ACNo = ACNo;
    }

    public String getChanPinZhongLei() {
        return ChanPinZhongLei;
    }

    public void setChanPinZhongLei(String chanPinZhongLei) {
        ChanPinZhongLei = chanPinZhongLei;
    }

    public String getGuiMianZhiQuFangShi() {
        return GuiMianZhiQuFangShi;
    }

    public void setGuiMianZhiQuFangShi(String guiMianZhiQuFangShi) {
        GuiMianZhiQuFangShi = guiMianZhiQuFangShi;
    }

    public String getKaiHuYinHang() {
        return KaiHuYinHang;
    }

    public void setKaiHuYinHang(String kaiHuYinHang) {
        KaiHuYinHang = kaiHuYinHang;
    }

    public String getYinHangLianXiDianHua() {
        return YinHangLianXiDianHua;
    }

    public void setYinHangLianXiDianHua(String yinHangLianXiDianHua) {
        YinHangLianXiDianHua = yinHangLianXiDianHua;
    }

    public String getCunZheCeHao() {
        return CunZheCeHao;
    }

    public void setCunZheCeHao(String cunZheCeHao) {
        CunZheCeHao = cunZheCeHao;
    }

    public String getTongCunTongDuiFangShi() {
        return TongCunTongDuiFangShi;
    }

    public void setTongCunTongDuiFangShi(String tongCunTongDuiFangShi) {
        TongCunTongDuiFangShi = tongCunTongDuiFangShi;
    }

    public String getZhiFuXiTongHangHao() {
        return ZhiFuXiTongHangHao;
    }

    public void setZhiFuXiTongHangHao(String zhiFuXiTongHangHao) {
        ZhiFuXiTongHangHao = zhiFuXiTongHangHao;
    }

    public String getJingBanGuiYuan() {
        return JingBanGuiYuan;
    }

    public void setJingBanGuiYuan(String jingBanGuiYuan) {
        JingBanGuiYuan = jingBanGuiYuan;
    }

    public String getIssueDate() {
        return IssueDate;
    }

    public void setIssueDate(String issueDate) {
        IssueDate = issueDate;
    }

    public String getKaiHuDate() {
        return KaiHuDate;
    }

    public void setKaiHuDate(String kaiHuDate) {
        KaiHuDate = kaiHuDate;
    }

    public String getPassbookNo() {
        return PassbookNo;
    }

    public void setPassbookNo(String passbookNo) {
        PassbookNo = passbookNo;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public CunZheBack(String tradeId, String ACName, String ACNo, String chanPinZhongLei, String guiMianZhiQuFangShi, String kaiHuYinHang, String yinHangLianXiDianHua, String cunZheCeHao, String tongCunTongDuiFangShi, String zhiFuXiTongHangHao, String jingBanGuiYuan, String issueDate, String kaiHuDate, String passbookNo) {
        this.tradeId = tradeId;
        this.ACName = ACName;
        this.ACNo = ACNo;
        this.ChanPinZhongLei = chanPinZhongLei;
        this.GuiMianZhiQuFangShi = guiMianZhiQuFangShi;
        this.KaiHuYinHang = kaiHuYinHang;
        this.YinHangLianXiDianHua = yinHangLianXiDianHua;
        this.CunZheCeHao = cunZheCeHao;
        this.TongCunTongDuiFangShi = tongCunTongDuiFangShi;
        this.ZhiFuXiTongHangHao = zhiFuXiTongHangHao;
        this.JingBanGuiYuan = jingBanGuiYuan;
        this.IssueDate = issueDate;
        this.KaiHuDate = kaiHuDate;
        this.PassbookNo = passbookNo;
    }

    @Override
    public String toString() {
        return "CunZheBack{" +
                "tradeId='" + tradeId + '\'' +
                ", ACName='" + ACName + '\'' +
                ", ACNo='" + ACNo + '\'' +
                ", ChanPinZhongLei='" + ChanPinZhongLei + '\'' +
                ", GuiMianZhiQuFangShi='" + GuiMianZhiQuFangShi + '\'' +
                ", KaiHuYinHang='" + KaiHuYinHang + '\'' +
                ", YinHangLianXiDianHua='" + YinHangLianXiDianHua + '\'' +
                ", CunZheCeHao='" + CunZheCeHao + '\'' +
                ", TongCunTongDuiFangShi='" + TongCunTongDuiFangShi + '\'' +
                ", ZhiFuXiTongHangHao='" + ZhiFuXiTongHangHao + '\'' +
                ", JingBanGuiYuan='" + JingBanGuiYuan + '\'' +
                ", IssueDate='" + IssueDate + '\'' +
                ", KaiHuDate='" + KaiHuDate + '\'' +
                ", PassbookNo='" + PassbookNo + '\'' +
                '}';
    }
}
