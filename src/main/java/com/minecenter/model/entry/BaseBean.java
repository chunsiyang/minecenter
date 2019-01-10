package com.minecenter.model.entry;

import javax.persistence.Column;
import java.util.Date;

public class BaseBean {

    /**
     * 创建人
     */
    @Column(name = "creat_id")
    private String creatId;

    /**
     * 创建时间
     */
    @Column(name = "creat_date")
    private Date creatDate;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private String updateId;

    /**
     * 更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 数据状态(0:删除，1:正常)
     */
    @Column(name = "del_flag")
    private Integer delFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 获取创建人
     *
     * @return creat_id - 创建人
     */
    public String getCreatId() {
        return creatId;
    }

    /**
     * 设置创建人
     *
     * @param creatId 创建人
     */
    public void setCreatId(String creatId) {
        this.creatId = creatId == null ? null : creatId.trim();
    }

    /**
     * 获取创建时间
     *
     * @return creat_date - 创建时间
     */
    public Date getCreatDate() {
        return creatDate;
    }

    /**
     * 设置创建时间
     *
     * @param creatDate 创建时间
     */
    public void setCreatDate(Date creatDate) {
        this.creatDate = creatDate;
    }

    /**
     * 获取更新人
     *
     * @return update_id - 更新人
     */
    public String getUpdateId() {
        return updateId;
    }

    /**
     * 设置更新人
     *
     * @param updateId 更新人
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId == null ? null : updateId.trim();
    }

    /**
     * 获取更新时间
     *
     * @return update_date - 更新时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置更新时间
     *
     * @param updateDate 更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取数据状态(0:删除，1:正常)
     *
     * @return del_flag - 数据状态(0:删除，1:正常)
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * 设置数据状态(0:删除，1:正常)
     *
     * @param delFlag 数据状态(0:删除，1:正常)
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}
