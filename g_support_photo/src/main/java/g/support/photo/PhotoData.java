package g.support.photo;

import java.io.Serializable;

class PhotoData implements Serializable {
    public String photoFilePath;
    public String photoUrl;
    public String photoAlbumName;
    public int photoFromFlag;
    public long photoFileSize;
    public boolean isSelect;

    public PhotoData(String photoFilePath, int photoFromFlag, long photoFileSize, boolean isSelect) {
        this.photoFilePath = photoFilePath;
        this.photoFromFlag = photoFromFlag;
        this.photoFileSize = photoFileSize;
        this.isSelect = isSelect;
    }
}
