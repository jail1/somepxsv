package ro.activemall.photoxserver.json;

public class PaginationJSON {
	private int page = 1;// which page
	private int size = 10;// page size
	private String sort = "id ASC";// sort field

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page > 0)
			this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		if (size > 0)
			this.size = size;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		if (sort != null && !"".equals(sort))
			this.sort = sort;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Page = ").append(getPage()).append("|");
		sb.append("Size = ").append(getSize()).append("|");
		sb.append("Order = '").append(getSort()).append("'").toString();
		return sb.toString();
	}
}
