package com.dzikirqu.android.util.tajweed.rule;

import com.dzikirqu.android.util.tajweed.model.Result;

import java.util.List;

public interface Rule {
  List<Result> checkAyah(String ayah);
}
