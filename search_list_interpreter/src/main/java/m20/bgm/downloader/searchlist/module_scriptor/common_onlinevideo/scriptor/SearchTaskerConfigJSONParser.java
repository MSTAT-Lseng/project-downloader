package m20.bgm.downloader.searchlist.module_scriptor.common_onlinevideo.scriptor;

public class SearchTaskerConfigJSONParser {

    private String search_method;
    private String search_link;
    private String data_params;
    private boolean enabled_pages_mode;
    private int timeout_seconds;
    public void setSearch_method(String search_method) {
        this.search_method = search_method;
    }
    public String getSearch_method() {
        return search_method;
    }

    public void setSearch_link(String search_link) {
        this.search_link = search_link;
    }
    public String getSearch_link() {
        return search_link;
    }

    public void setData_params(String data_params) {
        this.data_params = data_params;
    }
    public String getData_params() {
        return data_params;
    }

    public void setEnabled_pages_mode(boolean enabled_pages_mode) {
        this.enabled_pages_mode = enabled_pages_mode;
    }
    public boolean getEnabled_pages_mode() {
        return enabled_pages_mode;
    }

    public void setTimeout_seconds(int timeout_seconds) {
        this.timeout_seconds = timeout_seconds;
    }
    public int getTimeout_seconds() {
        return timeout_seconds;
    }

}
