package com.mapbox.navigation.ui.map;


import androidx.annotation.NonNull;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import java.util.ArrayList;
import java.util.List;

class NavigationSymbolManager {

  static final String MAPBOX_NAVIGATION_MARKER_NAME = "mapbox-navigation-marker";
  private final List<Symbol> mapMarkersSymbols = new ArrayList<>();
  private Symbol destinationSymbol = null;
  @NonNull
  private final SymbolManager symbolManager;

  NavigationSymbolManager(@NonNull SymbolManager symbolManager) {
    this.symbolManager = symbolManager;
    symbolManager.setIconAllowOverlap(true);
    symbolManager.setIconIgnorePlacement(true);
  }

  void addDestinationMarkerFor(@NonNull Point position) {
    if (destinationSymbol != null) {
      symbolManager.delete(destinationSymbol);
      mapMarkersSymbols.remove(destinationSymbol);
    }
    SymbolOptions options = createSymbolOptionsFor(position);
    destinationSymbol = createSymbolFrom(options);
  }

  void addCustomSymbolFor(@NonNull SymbolOptions options) {
    createSymbolFrom(options);
  }

  void removeAllMarkerSymbols() {
    for (Symbol markerSymbol : mapMarkersSymbols) {
      symbolManager.delete(markerSymbol);
    }
    mapMarkersSymbols.clear();
    destinationSymbol = null;
  }

  @NonNull
  private SymbolOptions createSymbolOptionsFor(@NonNull Point position) {
    LatLng markerPosition = new LatLng(position.latitude(),
      position.longitude());
    return new SymbolOptions()
      .withLatLng(markerPosition)
      .withIconImage(MAPBOX_NAVIGATION_MARKER_NAME);
  }

  private Symbol createSymbolFrom(@NonNull SymbolOptions options) {
    Symbol symbol = symbolManager.create(options);
    mapMarkersSymbols.add(symbol);
    return symbol;
  }
}
