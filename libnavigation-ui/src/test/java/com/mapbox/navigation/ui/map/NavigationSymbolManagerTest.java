package com.mapbox.navigation.ui.map;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NavigationSymbolManagerTest {

  @Test
  public void addDestinationMarkerFor_symbolManagerAddsOptions() {
    SymbolManager symbolManager = mock(SymbolManager.class);
    Symbol symbol = mock(Symbol.class);
    when(symbolManager.create(any(SymbolOptions.class))).thenReturn(symbol);
    NavigationSymbolManager navigationSymbolManager = new NavigationSymbolManager(symbolManager);
    Point position = Point.fromLngLat(1.2345, 1.3456);

    navigationSymbolManager.addDestinationMarkerFor(position);

    verify(symbolManager).create(any(SymbolOptions.class));
  }

  @Test
  public void addDestinationMarkerFor_destinationSymbolRemovedIfPreviouslyAdded() {
    SymbolManager symbolManager = mock(SymbolManager.class);
    Symbol oldDestinationSymbol = mock(Symbol.class);
    Symbol currentDestinationSymbol = mock(Symbol.class);
    when(symbolManager.create(any(SymbolOptions.class))).thenReturn(oldDestinationSymbol, currentDestinationSymbol);
    NavigationSymbolManager navigationSymbolManager = new NavigationSymbolManager(symbolManager);
    Point position = Point.fromLngLat(1.2345, 1.3456);

    navigationSymbolManager.addDestinationMarkerFor(position);
    navigationSymbolManager.addDestinationMarkerFor(position);

    verify(symbolManager, times(2)).create(any(SymbolOptions.class));
    verify(symbolManager, times(1)).delete(eq(oldDestinationSymbol));
  }

  @Test
  public void clearAllMarkerSymbols_previouslyAddedMarkersCleared() {
    SymbolManager symbolManager = mock(SymbolManager.class);
    Symbol symbol = mock(Symbol.class);
    when(symbolManager.create(any(SymbolOptions.class))).thenReturn(symbol);
    NavigationSymbolManager navigationSymbolManager = new NavigationSymbolManager(symbolManager);
    Point position = Point.fromLngLat(1.2345, 1.3456);
    navigationSymbolManager.addDestinationMarkerFor(position);

    navigationSymbolManager.clearAllMarkerSymbols();

    verify(symbolManager).delete(symbol);
  }

  @Test
  public void addCustomSymbolFor_symbolManagerCreatesSymbol() {
    SymbolManager symbolManager = mock(SymbolManager.class);
    Symbol symbol = mock(Symbol.class);
    SymbolOptions symbolOptions = mock(SymbolOptions.class);
    when(symbolManager.create(symbolOptions)).thenReturn(symbol);
    NavigationSymbolManager navigationSymbolManager = new NavigationSymbolManager(symbolManager);

    navigationSymbolManager.addCustomSymbolFor(symbolOptions);

    verify(symbolManager).create(symbolOptions);
  }

  @Test
  public void clearSymbolWithId_previouslyAddedMarkersCleared() {
    Symbol symbol = mock(Symbol.class);
    long symbolId = 911L;
    when(symbol.getId()).thenReturn(symbolId);
    SymbolManager symbolManager = mock(SymbolManager.class);
    SymbolOptions symbolOptions = mock(SymbolOptions.class);
    when(symbolManager.create(symbolOptions)).thenReturn(symbol);
    NavigationSymbolManager navigationSymbolManager = new NavigationSymbolManager(symbolManager);
    navigationSymbolManager.addCustomSymbolFor(symbolOptions);

    navigationSymbolManager.clearSymbolWithId(symbolId);

    verify(symbolManager).delete(symbol);
  }

  @Test
  public void clearSymbolWithId_symbolBeClearedOnlyOnce() {
    Symbol symbol = mock(Symbol.class);
    long symbolId = 911L;
    when(symbol.getId()).thenReturn(symbolId);
    SymbolManager symbolManager = mock(SymbolManager.class);
    SymbolOptions symbolOptions = mock(SymbolOptions.class);
    when(symbolManager.create(symbolOptions)).thenReturn(symbol);
    NavigationSymbolManager navigationSymbolManager = new NavigationSymbolManager(symbolManager);
    navigationSymbolManager.addCustomSymbolFor(symbolOptions);

    navigationSymbolManager.clearSymbolWithId(symbolId);
    navigationSymbolManager.clearSymbolWithId(symbolId);

    verify(symbolManager, times(1)).delete(symbol);
  }

  @Test
  public void clearSymbolsWithIconImageProperty_sameIconImageSymbolsCleared() {
    String iconImageFeedback = "feedback";
    Symbol aFeedbackSymbol = mock(Symbol.class);
    when(aFeedbackSymbol.getId()).thenReturn(0L);
    when(aFeedbackSymbol.getIconImage()).thenReturn(iconImageFeedback);
    Symbol bFeedbackSymbol = mock(Symbol.class);
    when(bFeedbackSymbol.getId()).thenReturn(1L);
    when(bFeedbackSymbol.getIconImage()).thenReturn(iconImageFeedback);
    String iconImageRandom = "random";
    Symbol aSymbol = mock(Symbol.class);
    when(aSymbol.getId()).thenReturn(2L);
    when(aSymbol.getIconImage()).thenReturn(iconImageRandom);
    SymbolManager symbolManager = mock(SymbolManager.class);
    SymbolOptions symbolOptions = mock(SymbolOptions.class);
    when(symbolManager.create(symbolOptions)).thenReturn(aFeedbackSymbol, bFeedbackSymbol, aSymbol);
    NavigationSymbolManager navigationSymbolManager = new NavigationSymbolManager(symbolManager);
    navigationSymbolManager.addCustomSymbolFor(symbolOptions);
    navigationSymbolManager.addCustomSymbolFor(symbolOptions);
    navigationSymbolManager.addCustomSymbolFor(symbolOptions);

    navigationSymbolManager.clearSymbolsWithIconImageProperty(iconImageFeedback);

    verify(symbolManager).delete(aFeedbackSymbol);
    verify(symbolManager).delete(bFeedbackSymbol);
    verify(symbolManager, times(0)).delete(aSymbol);
  }
}