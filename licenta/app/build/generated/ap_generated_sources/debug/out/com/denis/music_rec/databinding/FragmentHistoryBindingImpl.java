package com.denis.music_rec.databinding;
import com.denis.music_rec.R;
import com.denis.music_rec.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentHistoryBindingImpl extends FragmentHistoryBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.fab, 1);
        sViewsWithIds.put(R.id.deleteAllBtn, 2);
        sViewsWithIds.put(R.id.change_theme, 3);
        sViewsWithIds.put(R.id.empty_image, 4);
        sViewsWithIds.put(R.id.no_data_text, 5);
        sViewsWithIds.put(R.id.lnContainer, 6);
        sViewsWithIds.put(R.id.appbar, 7);
        sViewsWithIds.put(R.id.toolbar, 8);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentHistoryBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds));
    }
    private FragmentHistoryBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.appbar.AppBarLayout) bindings[7]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[3]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[2]
            , (android.widget.ImageView) bindings[4]
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[1]
            , (android.widget.FrameLayout) bindings[0]
            , (android.widget.LinearLayout) bindings[6]
            , (android.widget.TextView) bindings[5]
            , (androidx.appcompat.widget.Toolbar) bindings[8]
            );
        this.historyContainer.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}