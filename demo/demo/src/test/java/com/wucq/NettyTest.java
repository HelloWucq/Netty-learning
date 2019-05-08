package com.wucq;

import java.util.Arrays;

/**
 * Unit test for simple App.
 */
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class NettyTest {

    @Test
    public void simpleUse() {
        ByteBuf buf = Unpooled.buffer(10);
        System.out.println("原始ByteBuf为***************" + buf.toString());
        System.out.println("ByteBuf中的内容为************" + Arrays.toString(buf.array()) + "\n");

        byte[] bytes = { 1, 2, 3, 4, 5 };
        buf.writeBytes(bytes);
        System.out.println("写入的bytes为*************" + Arrays.toString(bytes));
        System.out.println("写入一段内容后ByteBuf为***********" + buf.toString());
        System.out.println("ByteBuf中的内容为************" + Arrays.toString(buf.array()) + "\n");

        byte b1 = buf.readByte();
        byte b2 = buf.readByte();
        System.out.println("读取的bytes为*************" + Arrays.toString(new byte[] { b1, b2 }));
        System.out.println("读取一段内容后ByteBuf为***********" + buf.toString());
        System.out.println("ByteBuf中的内容为************" + Arrays.toString(buf.array()) + "\n");

        buf.discardReadBytes();
        System.out.println("将读取的内容丢弃后ByteBuf为************" + buf.toString());
        System.out.println("ByteBuf中的内容为************" + Arrays.toString(buf.array()) + "\n");

        buf.clear();
        System.out.println("将读写指针清空后ByteBuf为*************" + buf.toString());
        System.out.println("ByteBuf中的内容为************" + Arrays.toString(buf.array()) + "\n");

        byte[] bytes2 = { 1, 2, 3 };
        buf.writeBytes(bytes2);
        System.out.println("写入的bytes为*************" + Arrays.toString(bytes));
        System.out.println("写入一段内容后ByteBuf为***********" + buf.toString());
        System.out.println("ByteBuf中的内容为************" + Arrays.toString(buf.array()) + "\n");

        buf.setZero(0, buf.capacity());
        System.out.println("将内容清零后ByteBuf为***************" + buf.toString());
        System.out.println("ByteBuf中的内容为************" + Arrays.toString(buf.array()) + "\n");

        byte[] bytes3 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
        buf.writeBytes(bytes3);
        System.out.println("写入的bytes为*************" + Arrays.toString(bytes));
        System.out.println("写入一段内容后ByteBuf为***********" + buf.toString());
        System.out.println("ByteBuf中的内容为************" + Arrays.toString(buf.array()) + "\n");
    }

    @Test
    public void simpleTest2() {
        PooledByteBufAllocator pool = new PooledByteBufAllocator();
        CompositeByteBuf buf = new CompositeByteBuf(pool, true, 2000);
        byte[] b1 = { 1, 2, 3 };
        byte[] b2 = { 3, 4, 5 };
        buf.addComponent(Unpooled.copiedBuffer(b1));
        buf.addComponent(Unpooled.copiedBuffer(b2));
        byte b = buf.getByte(5);
        System.out.println(b);
    }

    @Test
    public void simpleTest3() {
        ByteBuf buf = Unpooled.copiedBuffer("Netty in action", CharsetUtil.UTF_8);
        ByteBuf sliced = buf.slice(0, 15);
        System.out.println(sliced.toString());
        buf.setByte(0, (byte) 'J');
        if (buf.getByte(0) == sliced.getByte(0)) {
            System.out.println("Yes");
        }
    }

    @Test
    public void simpleTest4() {
        ByteBuf buf = Unpooled.copiedBuffer("Netty in action", CharsetUtil.UTF_8);
        ByteBuf copy = buf.copy(0, 15);
        System.out.println(copy.toString());
        buf.setByte(0, (byte) 'J');
        if (buf.getByte(0) == copy.getByte(0)) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
    }

    @Test
    public void simpleTest5() {
        ByteBuf heapBuf = Unpooled.copiedBuffer("heap space", CharsetUtil.UTF_8);
        if (heapBuf.hasArray()) {
            byte[] array = heapBuf.array();
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            System.out.println(Arrays.toString(array));
            System.out.println(offset);
            System.out.println(length);
        } else {
            System.out.println("No heap Array");
        }
    }

    @Test
    public void simpleTest6() {
        ByteBuf directBuf = Unpooled.directBuffer(100);
        directBuf.writeBytes("direct buffer".getBytes());
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
            directBuf.getBytes(directBuf.readerIndex(), array);
            System.out.println(Arrays.toString(array));
            System.out.println(length);
        } else {
            System.out.println("No direct Array");
        }
    }

    @Test
    public void simpleTest7() {
        
        CompositeByteBuf messageBuf=Unpooled.compositeBuffer();
        ByteBuf headerBuf=Unpooled.copiedBuffer("head", CharsetUtil.UTF_8);
        ByteBuf bodyBuf=Unpooled.copiedBuffer("body", CharsetUtil.UTF_8);
        messageBuf.addComponents(headerBuf,bodyBuf);
        System.out.println("Remove Head Before*******");
        printCompositeBuffer(messageBuf);
        for(ByteBuf buf:messageBuf){
            System.out.println(buf.toString(CharsetUtil.UTF_8));
        }

        System.out.println("Remove Head After*******");
        messageBuf.removeComponent(0);
        printCompositeBuffer(messageBuf);
        for(ByteBuf buf:messageBuf){
            System.out.println(buf.toString(CharsetUtil.UTF_8));
        }
    }

    public static void printCompositeBuffer(CompositeByteBuf compositeByteBuf) {
        int length=compositeByteBuf.readableBytes();
        byte[] array=new byte[length];
        compositeByteBuf.getBytes(compositeByteBuf.readerIndex(), array);
        System.out.println(Arrays.toString(array));
        System.out.println(length);
        
    }
}
