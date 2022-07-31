package com.wagyufari.dzikirqu.util.tajweed.rule;

import com.wagyufari.dzikirqu.util.tajweed.model.Result;

import java.util.List;

public interface Rule {
  List<Result> checkAyah(String ayah);
}
