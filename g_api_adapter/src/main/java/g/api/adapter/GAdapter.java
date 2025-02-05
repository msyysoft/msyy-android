package g.api.adapter;

import java.util.List;

public interface GAdapter<MyAdapterData> {

    public void setDatas(List<MyAdapterData> datas);

    public void addDatas(List<MyAdapterData> datas);

    public List<MyAdapterData> getDatas();

    public boolean hasDatas();

    public int getRealCount();
}
