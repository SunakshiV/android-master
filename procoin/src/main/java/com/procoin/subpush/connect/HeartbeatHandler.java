package com.procoin.subpush.connect;

import com.procoin.subpush.Consts;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;


public class HeartbeatHandler extends IdleStateAwareChannelHandler{
	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e)throws Exception {
		if (e.getState() == IdleState.READER_IDLE) {
			ctx.getChannel().close();
        }else if (e.getState() == IdleState.WRITER_IDLE) {
        	ctx.getChannel().write(Consts.PING + Consts.R_N).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (!future.isSuccess()) {
						future.getChannel().close();
					}
				}
			});
        }
	}
}
