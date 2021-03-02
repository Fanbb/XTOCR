package com.ocr.system.model;
import com.ocr.common.utils.StringUtils;
/**
 * 存折
 * @author Jocker
 * @date 2021/3/1
 */
public class CunZhe {
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
    /**
     * 图片类型
     */
    private String imgType;
    /**
     * 识别结果
     */
    private String flag;
    /**
     * 预警结果
     */
    private String riskFlag;

    public CunZhe() {
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

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getRiskFlag() {
        return riskFlag;
    }

    public void setRiskFlag(String riskFlag) {
        this.riskFlag = riskFlag;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    @Override
    public String toString() {
        return "CunZhe{" +
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
                ", imgType='" + imgType + '\'' +
                ", flag='" + flag + '\'' +
                ", riskFlag='" + riskFlag + '\'' +
                '}';
    }
    public boolean hasEmptyField() {
            return StringUtils.isEmpty(getACName())||StringUtils.isEmpty(getACNo())||StringUtils.isEmpty(getChanPinZhongLei())||StringUtils.isEmpty(getCunZheCeHao())||StringUtils.isEmpty(getGuiMianZhiQuFangShi())||StringUtils.isEmpty(getIssueDate())||StringUtils.isEmpty(getJingBanGuiYuan())||StringUtils.isEmpty(getKaiHuDate())||StringUtils.isEmpty(getKaiHuYinHang())||StringUtils.isEmpty(getPassbookNo())||StringUtils.isEmpty(getTongCunTongDuiFangShi())||StringUtils.isEmpty(getYinHangLianXiDianHua())||StringUtils.isEmpty(getZhiFuXiTongHangHao());
}       }
