package com.denis.music_rec;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.denis.music_rec.databinding.FragmentHistoryBindingImpl;
import com.denis.music_rec.databinding.FragmentMainBindingImpl;
import com.denis.music_rec.databinding.FragmentMusicBindingImpl;
import com.denis.music_rec.databinding.FragmentNotFoundBindingImpl;
import com.denis.music_rec.databinding.ViewMusicBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_FRAGMENTHISTORY = 1;

  private static final int LAYOUT_FRAGMENTMAIN = 2;

  private static final int LAYOUT_FRAGMENTMUSIC = 3;

  private static final int LAYOUT_FRAGMENTNOTFOUND = 4;

  private static final int LAYOUT_VIEWMUSIC = 5;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(5);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.denis.music_rec.R.layout.fragment_history, LAYOUT_FRAGMENTHISTORY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.denis.music_rec.R.layout.fragment_main, LAYOUT_FRAGMENTMAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.denis.music_rec.R.layout.fragment_music, LAYOUT_FRAGMENTMUSIC);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.denis.music_rec.R.layout.fragment_not_found, LAYOUT_FRAGMENTNOTFOUND);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.denis.music_rec.R.layout.view_music, LAYOUT_VIEWMUSIC);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_FRAGMENTHISTORY: {
          if ("layout/fragment_history_0".equals(tag)) {
            return new FragmentHistoryBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_history is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTMAIN: {
          if ("layout/fragment_main_0".equals(tag)) {
            return new FragmentMainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_main is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTMUSIC: {
          if ("layout/fragment_music_0".equals(tag)) {
            return new FragmentMusicBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_music is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTNOTFOUND: {
          if ("layout/fragment_not_found_0".equals(tag)) {
            return new FragmentNotFoundBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_not_found is invalid. Received: " + tag);
        }
        case  LAYOUT_VIEWMUSIC: {
          if ("layout/view_music_0".equals(tag)) {
            return new ViewMusicBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for view_music is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(1);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(5);

    static {
      sKeys.put("layout/fragment_history_0", com.denis.music_rec.R.layout.fragment_history);
      sKeys.put("layout/fragment_main_0", com.denis.music_rec.R.layout.fragment_main);
      sKeys.put("layout/fragment_music_0", com.denis.music_rec.R.layout.fragment_music);
      sKeys.put("layout/fragment_not_found_0", com.denis.music_rec.R.layout.fragment_not_found);
      sKeys.put("layout/view_music_0", com.denis.music_rec.R.layout.view_music);
    }
  }
}
