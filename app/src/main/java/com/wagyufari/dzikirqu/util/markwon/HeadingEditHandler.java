package com.wagyufari.dzikirqu.util.markwon;

import android.text.Editable;
import android.text.Spanned;

import androidx.annotation.NonNull;

import io.noties.markwon.Markwon;
import io.noties.markwon.core.MarkwonTheme;
import io.noties.markwon.core.spans.HeadingSpan;
import io.noties.markwon.editor.EditHandler;
import io.noties.markwon.editor.PersistedSpans;

public class HeadingEditHandler implements EditHandler<HeadingSpan> {

  private MarkwonTheme theme;

  @Override
  public void init(@NonNull Markwon markwon) {
    this.theme = markwon.configuration().theme();
  }

  @Override
  public void configurePersistedSpans(@NonNull PersistedSpans.Builder builder) {
    builder
      .persistSpan(Head1.class, () -> new Head1(theme))
      .persistSpan(Head2.class, () -> new Head2(theme))
      .persistSpan(Head3.class, () -> new Head3(theme))
      .persistSpan(Head4.class, () -> new Head4(theme))
      .persistSpan(Head5.class, () -> new Head5(theme))
      .persistSpan(Head6.class, () -> new Head6(theme));
  }

  @Override
  public void handleMarkdownSpan(
    @NonNull PersistedSpans persistedSpans,
    @NonNull Editable editable,
    @NonNull String input,
    @NonNull HeadingSpan span,
    int spanStart,
    int spanTextLength
  ) {
    final Class<?> type;
    switch (span.getLevel()) {
      case 1:
        type = Head1.class;
        break;
      case 2:
        type = Head2.class;
        break;
      case 3:
        type = Head3.class;
        break;
      case 4:
        type = Head4.class;
        break;
      case 5:
        type = Head5.class;
        break;
      case 6:
        type = Head6.class;
        break;
      default:
        type = null;
    }

    if (type != null) {
      final int index = input.indexOf('\n', spanStart + spanTextLength);
      final int end = index < 0
        ? input.length()
        : index;
      editable.setSpan(
        persistedSpans.get(type),
        spanStart,
        end,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      );
    }
  }

  @NonNull
  @Override
  public Class<HeadingSpan> markdownSpanType() {
    return HeadingSpan.class;
  }

  private static class Head1 extends HeadingSpan {
    Head1(@NonNull MarkwonTheme theme) {
      super(theme, 1);
    }
  }

  private static class Head2 extends HeadingSpan {
    Head2(@NonNull MarkwonTheme theme) {
      super(theme, 2);
    }
  }
  private static class Head3 extends HeadingSpan {
    Head3(@NonNull MarkwonTheme theme) {
      super(theme, 3);
    }
  }
  private static class Head4 extends HeadingSpan {
    Head4(@NonNull MarkwonTheme theme) {
      super(theme, 4);
    }
  }
  private static class Head5 extends HeadingSpan {
    Head5(@NonNull MarkwonTheme theme) {
      super(theme, 5);
    }
  }
  private static class Head6 extends HeadingSpan {
    Head6(@NonNull MarkwonTheme theme) {
      super(theme, 6);
    }
  }
}
