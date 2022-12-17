package org.javaboy.tienchin.channel.mapper;

import org.javaboy.tienchin.channel.domain.Channel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.javaboy.tienchin.channel.domain.vo.ChannelVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author javaboy
 * @since 2022-12-03
 */
public interface ChannelMapper extends BaseMapper<Channel> {

    List<Channel> selectChannelList(ChannelVO channelVO);
}
