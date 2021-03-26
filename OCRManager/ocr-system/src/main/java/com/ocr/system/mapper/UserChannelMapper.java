package com.ocr.system.mapper;

import com.ocr.system.domain.SysUserChannel;

import java.util.List;

/**
 * 用户渠道关联 数据层
 *
 * @author ocr
 * @date 2020-06-19
 */
public interface UserChannelMapper {
    /**
     * 查询用户渠道关联信息
     *
     * @param userId 用户渠道关联ID
     * @return 用户渠道关联信息
     */
    public SysUserChannel selectUserChannelById(Integer userId);

    /**
     * 查询用户渠道关联列表
     *
     * @param userChannel 用户渠道关联信息
     * @return 用户渠道关联集合
     */
    public List<SysUserChannel> selectUserChannelList(SysUserChannel userChannel);

    /**
     * 新增用户渠道关联
     *
     * @param userChannel 用户渠道关联信息
     * @return 结果
     */
    public int insertUserChannel(SysUserChannel userChannel);

    /**
     * 修改用户渠道关联
     *
     * @param userChannel 用户渠道关联信息
     * @return 结果
     */
    public int updateUserChannel(SysUserChannel userChannel);

    /**
     * 删除用户渠道关联
     *
     * @param userId 用户渠道关联ID
     * @return 结果
     */
    public int deleteUserChannelById(Integer userId);

    /**
     * 批量删除用户渠道关联
     *
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserChannelByIds(String[] userIds);

    void batchUserChannel(List<SysUserChannel> list);

    void deleteUserChannelsById(Long userId);
}