package g.support.photo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 相册数据
 */
class PhotoAlbumData implements Serializable {
    public String albumName;
    public PhotoData firstData;
    public List<PhotoData> datas;

    public PhotoAlbumData() {
        this(null);
    }

    public PhotoAlbumData(List<PhotoData> datas) {
        if (datas != null && datas.size() != 0) {
            this.datas = datas;
            firstData = datas.get(0);
        } else {
            this.datas = new ArrayList<PhotoData>();
        }
    }
}
