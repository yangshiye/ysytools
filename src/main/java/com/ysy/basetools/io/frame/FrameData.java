package com.ysy.basetools.io.frame;

/**
 * Created by guoqiang on 2017/8/18.
 */
public class FrameData {
    private byte[] headBytes;//头信息 包括 FLAG(1字节) TYPE(2字节) LENGTH(4字节) 共7字节
    private byte[] dataBytes;//真实内容信息 弓LENGTH字节
    private byte[] endBytes;//尾部信息 VERIFY(4字节) FLAG(1字节) 共5字节
    private int idx = 0;
    private short type = 0;
    private int dataLength = 0;
    private int orgVerifyCode = 0;
    private int calcVerifyCode = 0;

    public byte[] getHeadBytes() {
        return headBytes;
    }

    public byte[] getEndBytes() {
        return endBytes;
    }

    public FrameData() {
        headBytes = new byte[FrameUtil.HEAD_LENGTH];
        endBytes = new byte[FrameUtil.END_LENGTH];
    }

    public FrameData(byte[] headBytes, byte[] dataBytes, byte[] endBytes) {
        this.headBytes = headBytes;
        this.dataBytes = dataBytes;
        this.endBytes = endBytes;
    }

    public FrameStatus putByte(byte b) {
        if (idx < FrameUtil.HEAD_LENGTH) {
            headBytes[idx] = b;
        } else if (idx < FrameUtil.HEAD_LENGTH + dataLength) {
            dataBytes[idx - FrameUtil.HEAD_LENGTH] = b;
        } else {
            endBytes[idx - FrameUtil.HEAD_LENGTH - dataLength] = b;
        }
        idx++;
        if (idx == 1) {
            if (headBytes[0] != FrameUtil.FLAG) {
                return new ErrorFrameStatus("headByte is error,byte=" + headBytes[0]);
            }
        } else if (idx == 3) {
            type = FrameUtil.toShort(headBytes[1], headBytes[2]);
        } else if (idx == FrameUtil.HEAD_LENGTH) {
            dataLength = FrameUtil.toInt(headBytes[3], headBytes[4], headBytes[5], headBytes[6]);
            if (dataLength < 0) {
                return new ErrorFrameStatus("dataLength is error,dataLength=" + dataLength);
            }
            dataBytes = new byte[dataLength];
        } else if (idx == FrameUtil.HEAD_LENGTH + dataLength + FrameUtil.END_LENGTH) {
            calcVerifyCode = FrameUtil.calcVerify(dataBytes);
            orgVerifyCode = FrameUtil.toInt(endBytes[0], endBytes[1], endBytes[2], endBytes[3]);
            if (orgVerifyCode != calcVerifyCode) {
                return new ErrorFrameStatus("calcVerifyCode is error,orgVerifyCode=" + orgVerifyCode
                        + ",calcVerifyCode" + calcVerifyCode);
            } else if (endBytes[4] != FrameUtil.FLAG) {
                return new ErrorFrameStatus("headByte is error,byte=" + endBytes[4]);
            } else {
                return FrameStatusImpl.SUCCESS;
            }
        }
        return FrameStatusImpl.WAIT;
    }

    public int getIdx() {
        return idx;
    }

    public byte[] getDataBytes() {
        return dataBytes;
    }

    public short getType() {
        return type;
    }

    public int getDataLength() {
        return dataLength;
    }

    public int getOrgVerifyCode() {
        return orgVerifyCode;
    }

    public int getCalcVerifyCode() {
        return calcVerifyCode;
    }
}
