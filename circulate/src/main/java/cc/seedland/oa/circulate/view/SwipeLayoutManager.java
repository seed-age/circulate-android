package cc.seedland.oa.circulate.view;

/**
 * 作用：用来保存上一次打开的SwipeLayout
 */
public class SwipeLayoutManager {
	
	private static SwipeLayoutManager instance = new SwipeLayoutManager();

	private SwipeLayoutManager() {
	}
	
	public static SwipeLayoutManager getInstance() {
		return instance;
	}
	
	/** 上一次打开的SwipeLayout */
	private SwipeLayout mSwipeLayout;

	public SwipeLayout getSwipeLayout() {
		return mSwipeLayout;
	}

	/** 保存当前打开的SwipeLayout */
	public void setSwipeLayout(SwipeLayout swipeLayout) {
		this.mSwipeLayout = swipeLayout;
	}
	
	/** 关闭上一次打开的SwipeLayout */
	public void closeSwipeLayout() {
		if (mSwipeLayout != null) {
			mSwipeLayout.close();
			mSwipeLayout = null;
		}
	}
}












