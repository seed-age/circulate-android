package cc.seedland.oa.circulate.modle.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2018/1/30 0030.
 */

public class FileInfo implements MultiItemEntity{
    public String desc;
    public boolean dir;
    public long neid;
    public String path;
    public String pathType;
    public String size;
    public boolean isSelected;
    public static final int TYPE_DIR = 0x001;
    public static final int TYPE_FILE = 0x002;
    @Override
    public int getItemType() {
        return dir?TYPE_DIR:TYPE_FILE;
    }
}
