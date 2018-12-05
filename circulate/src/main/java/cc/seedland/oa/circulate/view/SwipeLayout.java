package cc.seedland.oa.circulate.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 侧滑删除控件
 */
public class SwipeLayout extends FrameLayout {

	/** 菜单布局 */
	private View mMenuView;
	
	/** 内容布局 */
	private View mContentView;

	private int mHeight;

	private int mWidth;

	/** 菜单布局的宽度 */
	private int mMenuWidth;

	private ViewDragHelper dragHelper;

	public SwipeLayout(Context context) {
		this(context, null);
	}

	public SwipeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	// 4. 处理Callback回调方法
	Callback mCallback = new Callback() {
		
		// （1）根据返回值决定是否可以拖动子控件, true表示可以拖动
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return true;
		}
		
		// （2）设置被拖动的控件水平方向将要显示的位置(left: 控件的左边界)
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			
			// 限制内容布局滑动的范围：[-mMenuWidth, 0]
			if (child == mContentView) {
				if (left < -mMenuWidth) {
					left = -mMenuWidth;
				} else if (left > 0) {
					left = 0;
				}
			}
			
			// 限制菜单布局滑动的范围：[mWidth-mMenuWidth, mWidth]
			if (child == mMenuView) {
				if (left < mWidth-mMenuWidth) {
					left = mWidth-mMenuWidth;
				} else if (left > mWidth) {
					left = mWidth;
				}
			}
			
			return left;
		};
		
		// (3) 当位置发生改变时调用[关联滑动，监听自定义事件(打开，关闭，拖动..)]
		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			// a. 关联滑动
			// 当滑动内容布局时，同时滑动菜单布局
			if (changedView == mContentView) {
				// 内容布局移动了dx，菜单布局也移动dx
				mMenuView.offsetLeftAndRight(dx);
			} else if (changedView == mMenuView) {
				// 菜单布局移动了dx，内容布局也移动dx
				mContentView.offsetLeftAndRight(dx);
			}
			
			// b.监听自定义事件(打开，关闭，拖动..)
			listenDragStatus();
			
			// 解决滑动时空白的问题
			invalidate();
		};
		
		// (4) 松开手时回调（需要平滑地打开或关闭SwipeLayout）
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			System.out.println("----xvel: " + xvel);
			
			if (xvel < 0) { // 往左甩时
				open();
			} else if (xvel == 0 && mContentView.getLeft() < -mMenuWidth/2) {
				// 中线的左边速度为0时松开手
				open();
			} else {
				close();
			}
		};
		
	};
	
	private void init() {
		// 1. 创建ViewDragHelper对象
		dragHelper = ViewDragHelper.create(this, mCallback);
	}
	
	//============监听自定义事件（begin）=========================
	public enum DragStatus {
		OPEN, CLOSE, DRAGGING
	}
	
	/** 当前状态 */
	private DragStatus mCrrentStatus = DragStatus.CLOSE;
	
	/**
	 * 监听自定义事件(打开，关闭，拖动..)
	 */
	protected void listenDragStatus() {
		// 根据内容布局的左边界面判断当前的状态
		int left = mContentView.getLeft();
		
		// 要变成的新状态
		DragStatus tempStatus;
		
		if (left == 0) {// 关闭
			tempStatus = DragStatus.CLOSE;
		} else if (left == -mMenuWidth) {	// 打开
			tempStatus = DragStatus.OPEN;
		} else {	// 正在拖动
			tempStatus = DragStatus.DRAGGING;
		}
		
		// 3.当事件发生时，调用监听器中对应的方法
		if (mOnDragListener != null) {
			if (tempStatus == DragStatus.CLOSE) {		// 关闭状态
				mOnDragListener.onClose(this);
			} else if (tempStatus == DragStatus.OPEN) {	// 打开状态
				mOnDragListener.onOpen(this);
			} else {										// 正在拖动状态
				mOnDragListener.onDragging(this);
				
				// 当前状态为关闭，要变成拖动状态，就表示准备要打开了
				if (mCrrentStatus == DragStatus.CLOSE) {
					mOnDragListener.onStartOpen(this);
				} else if (mCrrentStatus == DragStatus.OPEN) {
					// 当前状态为打开，要变成拖动状态，就表示准备要关闭了
					mOnDragListener.onStartClose(this);
				}
			}
		}
		
		mCrrentStatus = tempStatus;
	}
	
	// 1. 定义接口
	public interface OnDragListener {
		
		public void onOpen(SwipeLayout layout);
		public void onClose(SwipeLayout layout);
		public void onDragging(SwipeLayout layout);
		
		/** 准备打开 */
		public void onStartOpen(SwipeLayout layout);
		/** 准备关闭*/
		public void onStartClose(SwipeLayout layout);
	}
	
	// 2. 提供监听器变量和set方法
	public OnDragListener mOnDragListener;
	
	public void setOnDragListener(OnDragListener onDragListener) {
		this.mOnDragListener = onDragListener;
	}
	
	
	//============监听自定义事件（end）=========================


	/** 打开SwipeLayout */
	protected void open() {
		// 第1步：平滑打开
		dragHelper.smoothSlideViewTo(mContentView, -mMenuWidth, 0);
		// 刷新界面 invalidate() --> onDraw() --> computeScroll();
		ViewCompat.postInvalidateOnAnimation(this);
	}

	
	@Override
	public void computeScroll() {
		super.computeScroll();
		// 第2步：平滑打开
		if (dragHelper.continueSettling(true)) {
			// 刷新界面 invalidate() --> onDraw() --> computeScroll();
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}
	
	/** 关闭SwipeLayout */
	protected void close() {
		// 第1步：平滑关闭
		dragHelper.smoothSlideViewTo(mContentView, 0, 0);
		// 刷新界面 invalidate() --> onDraw() --> computeScroll();
		ViewCompat.postInvalidateOnAnimation(this);
	}

	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// 2. 由ViewDragHelper决定是否拦截
		return dragHelper.shouldInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		// 3. 把触摸事件传给ViewDragHelper处理
		dragHelper.processTouchEvent(event);
		
		// 按下时需要返回true,以便持续地接收到后续的move和up事件
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			return true;
		}
		
		return super.onTouchEvent(event);
	}
	

	// 填充结束时调用
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		// 键壮性判断
		if (getChildCount() < 2) {
			throw new IllegalStateException("swipeLayout至少要有两个子控件");
		}
		
		mMenuView = getChildAt(0);
		mContentView = getChildAt(1);
	}
	
	// 测量的方法
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		// 继承ViewGroup需要主动测量子控件的宽高
		// measureChildren(widthMeasureSpec, heightMeasureSpec);
		
		mHeight = getMeasuredHeight();
		mWidth = getMeasuredWidth();
		// 菜单布局的宽度
		mMenuWidth = mMenuView.getMeasuredWidth();
	}
	
	
//	@Override
//	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//		super.onSizeChanged(w, h, oldw, oldh);
//		// 在此方法获取宽高也可以
//	}
	
	
	// 设置子控件显示的位置
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// super.onLayout(changed, left, top, right, bottom);
		
		// 设置内容布局显示的位置
		mContentView.layout(0, 0, mWidth, mHeight);
		// 设置菜单布局显示的位置
		mMenuView.layout(mWidth, 0, mWidth+mMenuWidth, mHeight);
	}

}























