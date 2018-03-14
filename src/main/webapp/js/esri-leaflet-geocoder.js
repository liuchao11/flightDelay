/* esri-leaflet-geocoder - v2.0.3 - Wed Jan 27 2016 19:03:44 GMT-0800 (PST)
 * Copyright (c) 2016 Environmental Systems Research Institute, Inc.
 * Apache-2.0 */
(function (global, factory) {
    typeof exports === "object" && typeof module !== "undefined" ? factory(exports, require("leaflet"), require("esri-leaflet")) : typeof define === "function" && define.amd ? define(["exports", "leaflet", "esri-leaflet"], factory) : factory(global.L.esri.Geocoding = {}, L, L.esri)
})(this, function (exports, L, esri_leaflet) {
    "use strict";
    exports.Geocode = esri_leaflet.Task.extend({
        path: "find",
        params: {outSr: 4326, forStorage: false, outFields: "*", maxLocations: 20},
        setters: {
            address: "address",
            neighborhood: "neighborhood",
            city: "city",
            subregion: "subregion",
            region: "region",
            postal: "postal",
            country: "country",
            text: "text",
            category: "category",
            token: "token",
            key: "magicKey",
            fields: "outFields",
            forStorage: "forStorage",
            maxLocations: "maxLocations"
        },
        initialize: function (options) {
            options = options || {};
            options.url = options.url || exports.WorldGeocodingServiceUrl;
            esri_leaflet.Task.prototype.initialize.call(this, options)
        },
        within: function (bounds) {
            bounds = L.latLngBounds(bounds);
            this.params.bbox = esri_leaflet.Util.boundsToExtent(bounds);
            return this
        },
        nearby: function (latlng, radius) {
            latlng = L.latLng(latlng);
            this.params.location = latlng.lng + "," + latlng.lat;
            this.params.distance = Math.min(Math.max(radius, 2e3), 5e4);
            return this
        },
        run: function (callback, context) {
            this.path = this.params.text ? "find" : "findAddressCandidates";
            if (this.path === "findAddressCandidates" && this.params.bbox) {
                this.params.searchExtent = this.params.bbox;
                delete this.params.bbox
            }
            return this.request(function (error, response) {
                var processor = this.path === "find" ? this._processFindResponse : this._processFindAddressCandidatesResponse;
                var results = !error ? processor(response) : undefined;
                callback.call(context, error, {results: results}, response)
            }, this)
        },
        _processFindResponse: function (response) {
            var results = [];
            for (var i = 0; i < response.locations.length; i++) {
                var location = response.locations[i];
                var bounds;
                if (location.extent) {
                    bounds = esri_leaflet.Util.extentToBounds(location.extent)
                }
                results.push({
                    text: location.name,
                    bounds: bounds,
                    score: location.feature.attributes.Score,
                    latlng: L.latLng(location.feature.geometry.y, location.feature.geometry.x),
                    properties: location.feature.attributes
                })
            }
            return results
        },
        _processFindAddressCandidatesResponse: function (response) {
            var results = [];
            for (var i = 0; i < response.candidates.length; i++) {
                var candidate = response.candidates[i];
                var bounds = esri_leaflet.Util.extentToBounds(candidate.extent);
                results.push({
                    text: candidate.address,
                    bounds: bounds,
                    score: candidate.score,
                    latlng: L.latLng(candidate.location.y, candidate.location.x),
                    properties: candidate.attributes
                })
            }
            return results
        }
    });
    function geocode(options) {
        return new exports.Geocode(options)
    }

    exports.ReverseGeocode = esri_leaflet.Task.extend({
        path: "reverseGeocode",
        params: {outSR: 4326, returnIntersection: false},
        setters: {distance: "distance", language: "langCode", intersection: "returnIntersection"},
        initialize: function (options) {
            options = options || {};
            options.url = options.url || exports.WorldGeocodingServiceUrl;
            esri_leaflet.Task.prototype.initialize.call(this, options)
        },
        latlng: function (latlng) {
            latlng = L.latLng(latlng);
            this.params.location = latlng.lng + "," + latlng.lat;
            return this
        },
        run: function (callback, context) {
            return this.request(function (error, response) {
                var result;
                if (!error) {
                    result = {latlng: L.latLng(response.location.y, response.location.x), address: response.address}
                } else {
                    result = undefined
                }
                callback.call(context, error, result, response)
            }, this)
        }
    });
    function reverseGeocode(options) {
        return new exports.ReverseGeocode(options)
    }

    exports.Suggest = esri_leaflet.Task.extend({
        path: "suggest",
        params: {},
        setters: {text: "text", category: "category", countries: "countryCode"},
        initialize: function (options) {
            options = options || {};
            options.url = options.url || exports.WorldGeocodingServiceUrl;
            esri_leaflet.Task.prototype.initialize.call(this, options)
        },
        within: function (bounds) {
            bounds = L.latLngBounds(bounds);
            bounds = bounds.pad(.5);
            var center = bounds.getCenter();
            var ne = bounds.getNorthWest();
            this.params.location = center.lng + "," + center.lat;
            this.params.distance = Math.min(Math.max(center.distanceTo(ne), 2e3), 5e4);
            this.params.searchExtent = esri_leaflet.Util.boundsToExtent(bounds);
            return this
        },
        nearby: function (latlng, radius) {
            latlng = L.latLng(latlng);
            this.params.location = latlng.lng + "," + latlng.lat;
            this.params.distance = Math.min(Math.max(radius, 2e3), 5e4);
            return this
        },
        run: function (callback, context) {
            return this.request(function (error, response) {
                callback.call(context, error, response, response)
            }, this)
        }
    });
    function suggest(options) {
        return new exports.Suggest(options)
    }

    exports.GeocodeService = esri_leaflet.Service.extend({
        initialize: function (options) {
            options = options || {};
            options.url = options.url || exports.WorldGeocodingServiceUrl;
            esri_leaflet.Service.prototype.initialize.call(this, options);
            this._confirmSuggestSupport()
        }, geocode: function () {
            return geocode(this)
        }, reverse: function () {
            return reverseGeocode(this)
        }, suggest: function () {
            return suggest(this)
        }, _confirmSuggestSupport: function () {
            this.metadata(function (error, response) {
                if (error) {
                    return
                }
                if (response.capabilities.includes("Suggest")) {
                    this.options.supportsSuggest = true
                } else {
                    this.options.supportsSuggest = false
                }
            }, this)
        }
    });
    function geocodeService(options) {
        return new exports.GeocodeService(options)
    }

    exports.Geosearch = L.Control.extend({
        includes: L.Mixin.Events,
        options: {
            position: "topleft",
            zoomToResult: true,
            searchBounds: null,
            useMapBounds: 12,
            collapseAfterResult: true,
            expanded: false,
            allowMultipleResults: true,
            placeholder: "Search for places or addresses",
            title: "Location Search"
        },
        initialize: function (options) {
            L.Util.setOptions(this, options);
            if (!options || !options.providers || !options.providers.length) {
                throw new Error("You must specificy at least one provider")
            }
            this._providers = options.providers;
            for (var i = 0; i < this._providers.length; i++) {
                this._providers[i].addEventParent(this)
            }
            this._pendingSuggestions = [];
            L.Control.prototype.initialize.call(options)
        },
        _geocode: function (text, key, provider) {
            var activeRequests = 0;
            var allResults = [];
            var bounds;
            var callback = L.Util.bind(function (error, results) {
                activeRequests--;
                if (error) {
                    return
                }
                if (results) {
                    allResults = allResults.concat(results)
                }
                if (activeRequests <= 0) {
                    bounds = this._boundsFromResults(allResults);
                    this.fire("results", {
                        results: allResults,
                        bounds: bounds,
                        latlng: bounds ? bounds.getCenter() : undefined,
                        text: text
                    });
                    if (this.options.zoomToResult && bounds) {
                        this._map.fitBounds(bounds)
                    }
                    L.DomUtil.removeClass(this._input, "geocoder-control-loading");
                    this.fire("load");
                    this.clear();
                    this._input.blur()
                }
            }, this);
            if (key) {
                activeRequests++;
                provider.results(text, key, this._searchBounds(), callback)
            } else {
                for (var i = 0; i < this._providers.length; i++) {
                    activeRequests++;
                    this._providers[i].results(text, key, this._searchBounds(), callback)
                }
            }
        },
        _suggest: function (text) {
            L.DomUtil.addClass(this._input, "geocoder-control-loading");
            var activeRequests = this._providers.length;
            var createCallback = L.Util.bind(function (text, provider) {
                return L.Util.bind(function (error, suggestions) {
                    if (error) {
                        return
                    }
                    var i;
                    activeRequests = activeRequests - 1;
                    if (this._input.value < 2) {
                        this._suggestions.innerHTML = "";
                        this._suggestions.style.display = "none";
                        return
                    }
                    if (suggestions) {
                        for (i = 0; i < suggestions.length; i++) {
                            suggestions[i].provider = provider
                        }
                    }
                    if (provider._lastRender !== text && provider.nodes) {
                        for (i = 0; i < provider.nodes.length; i++) {
                            if (provider.nodes[i].parentElement) {
                                this._suggestions.removeChild(provider.nodes[i])
                            }
                        }
                        provider.nodes = []
                    }
                    if (suggestions.length && this._input.value === text) {
                        if (provider.nodes) {
                            for (var k = 0; k < provider.nodes.length; k++) {
                                if (provider.nodes[k].parentElement) {
                                    this._suggestions.removeChild(provider.nodes[k])
                                }
                            }
                        }
                        provider._lastRender = text;
                        provider.nodes = this._renderSuggestions(suggestions)
                    }
                    if (activeRequests === 0) {
                        L.DomUtil.removeClass(this._input, "geocoder-control-loading")
                    }
                }, this)
            }, this);
            this._pendingSuggestions = [];
            for (var i = 0; i < this._providers.length; i++) {
                var provider = this._providers[i];
                var request = provider.suggestions(text, this._searchBounds(), createCallback(text, provider));
                this._pendingSuggestions.push(request)
            }
        },
        _searchBounds: function () {
            if (this.options.searchBounds !== null) {
                return this.options.searchBounds
            }
            if (this.options.useMapBounds === false) {
                return null
            }
            if (this.options.useMapBounds === true) {
                return this._map.getBounds()
            }
            if (this.options.useMapBounds <= this._map.getZoom()) {
                return this._map.getBounds()
            }
            return null
        },
        _renderSuggestions: function (suggestions) {
            var currentGroup;
            this._suggestions.style.display = "block";
            this._suggestions.style.maxHeight = this._map.getSize().y - this._suggestions.offsetTop - this._wrapper.offsetTop - 10 + "px";
            var nodes = [];
            var list;
            var header;
            for (var i = 0; i < suggestions.length; i++) {
                var suggestion = suggestions[i];
                if (!header && this._providers.length > 1 && currentGroup !== suggestion.provider.options.label) {
                    header = L.DomUtil.create("span", "geocoder-control-header", this._suggestions);
                    header.textContent = suggestion.provider.options.label;
                    header.innerText = suggestion.provider.options.label;
                    currentGroup = suggestion.provider.options.label;
                    nodes.push(header)
                }
                if (!list) {
                    list = L.DomUtil.create("ul", "geocoder-control-list", this._suggestions)
                }
                var suggestionItem = L.DomUtil.create("li", "geocoder-control-suggestion", list);
                suggestionItem.innerHTML = suggestion.text;
                suggestionItem.provider = suggestion.provider;
                suggestionItem["data-magic-key"] = suggestion.magicKey
            }
            nodes.push(list);
            return nodes
        },
        _boundsFromResults: function (results) {
            if (!results.length) {
                return
            }
            var nullIsland = L.latLngBounds([0, 0], [0, 0]);
            var resultBounds = [];
            var resultLatlngs = [];
            for (var i = results.length - 1; i >= 0; i--) {
                var result = results[i];
                resultLatlngs.push(result.latlng);
                if (result.bounds && result.bounds.isValid() && !result.bounds.equals(nullIsland)) {
                    resultBounds.push(result.bounds)
                }
            }
            var bounds = L.latLngBounds(resultLatlngs);
            for (var j = 0; j < resultBounds.length; j++) {
                bounds.extend(resultBounds[i])
            }
            return bounds
        },
        clear: function () {
            this._suggestions.innerHTML = "";
            this._suggestions.style.display = "none";
            this._input.value = "";
            if (this.options.collapseAfterResult) {
                this._input.placeholder = "";
                L.DomUtil.removeClass(this._wrapper, "geocoder-control-expanded")
            }
            if (!this._map.scrollWheelZoom.enabled() && this._map.options.scrollWheelZoom) {
                this._map.scrollWheelZoom.enable()
            }
        },
        getAttribution: function () {
            var attribs = [];
            for (var i = 0; i < this._providers.length; i++) {
                if (this._providers[i].options.attribution) {
                    attribs.push(this._providers[i].options.attribution)
                }
            }
            return attribs.join(", ")
        },
        onAdd: function (map) {
            this._map = map;
            this._wrapper = L.DomUtil.create("div", "geocoder-control " + (this.options.expanded ? " " + "geocoder-control-expanded" : ""));
            this._input = L.DomUtil.create("input", "geocoder-control-input leaflet-bar", this._wrapper);
            this._input.title = this.options.title;
            this._suggestions = L.DomUtil.create("div", "geocoder-control-suggestions leaflet-bar", this._wrapper);
            var credits = this.getAttribution();
            map.attributionControl.addAttribution(credits);
            L.DomEvent.addListener(this._input, "focus", function (e) {
                this._input.placeholder = this.options.placeholder;
                L.DomUtil.addClass(this._wrapper, "geocoder-control-expanded")
            }, this);
            L.DomEvent.addListener(this._wrapper, "click", function (e) {
                L.DomUtil.addClass(this._wrapper, "geocoder-control-expanded");
                this._input.focus()
            }, this);
            L.DomEvent.addListener(this._suggestions, "mousedown", function (e) {
                var suggestionItem = e.target || e.srcElement;
                this._geocode(suggestionItem.innerHTML, suggestionItem["data-magic-key"], suggestionItem.provider);
                this.clear()
            }, this);
            L.DomEvent.addListener(this._input, "blur", function (e) {
                this.clear()
            }, this);
            L.DomEvent.addListener(this._input, "keydown", function (e) {
                L.DomUtil.addClass(this._wrapper, "geocoder-control-expanded");
                var list = this._suggestions.querySelectorAll("." + "geocoder-control-suggestion");
                var selected = this._suggestions.querySelectorAll("." + "geocoder-control-selected")[0];
                var selectedPosition;
                for (var i = 0; i < list.length; i++) {
                    if (list[i] === selected) {
                        selectedPosition = i;
                        break
                    }
                }
                switch (e.keyCode) {
                    case 13:
                        if (selected) {
                            this._geocode(selected.innerHTML, selected["data-magic-key"], selected.provider);
                            this.clear()
                        } else if (this.options.allowMultipleResults) {
                            this._geocode(this._input.value, undefined);
                            this.clear()
                        } else {
                            L.DomUtil.addClass(list[0], "geocoder-control-selected")
                        }
                        L.DomEvent.preventDefault(e);
                        break;
                    case 38:
                        if (selected) {
                            L.DomUtil.removeClass(selected, "geocoder-control-selected")
                        }
                        var previousItem = list[selectedPosition - 1];
                        if (selected && previousItem) {
                            L.DomUtil.addClass(previousItem, "geocoder-control-selected")
                        } else {
                            L.DomUtil.addClass(list[list.length - 1], "geocoder-control-selected")
                        }
                        L.DomEvent.preventDefault(e);
                        break;
                    case 40:
                        if (selected) {
                            L.DomUtil.removeClass(selected, "geocoder-control-selected")
                        }
                        var nextItem = list[selectedPosition + 1];
                        if (selected && nextItem) {
                            L.DomUtil.addClass(nextItem, "geocoder-control-selected")
                        } else {
                            L.DomUtil.addClass(list[0], "geocoder-control-selected")
                        }
                        L.DomEvent.preventDefault(e);
                        break;
                    default:
                        for (var x = 0; x < this._pendingSuggestions.length; x++) {
                            var request = this._pendingSuggestions[x];
                            if (request && request.abort && !request.id) {
                                request.abort()
                            }
                        }
                        break
                }
            }, this);
            L.DomEvent.addListener(this._input, "keyup", L.Util.throttle(function (e) {
                var key = e.which || e.keyCode;
                var text = (e.target || e.srcElement).value;
                if (text.length < 2) {
                    this._suggestions.innerHTML = "";
                    this._suggestions.style.display = "none";
                    L.DomUtil.removeClass(this._input, "geocoder-control-loading");
                    return
                }
                if (key === 27) {
                    this._suggestions.innerHTML = "";
                    this._suggestions.style.display = "none";
                    return
                }
                if (key !== 13 && key !== 38 && key !== 40) {
                    if (this._input.value !== this._lastValue) {
                        this._lastValue = this._input.value;
                        this._suggest(text)
                    }
                }
            }, 50, this), this);
            L.DomEvent.disableClickPropagation(this._wrapper);
            L.DomEvent.addListener(this._suggestions, "mouseover", function (e) {
                if (map.scrollWheelZoom.enabled() && map.options.scrollWheelZoom) {
                    map.scrollWheelZoom.disable()
                }
            });
            L.DomEvent.addListener(this._suggestions, "mouseout", function (e) {
                if (!map.scrollWheelZoom.enabled() && map.options.scrollWheelZoom) {
                    map.scrollWheelZoom.enable()
                }
            });
            return this._wrapper
        },
        onRemove: function (map) {
            map.attributionControl.removeAttribution("Geocoding by Esri")
        }
    });
    function geosearch(options) {
        return new exports.Geosearch(options)
    }

    exports.ArcgisOnlineProvider = exports.GeocodeService.extend({
        options: {
            label: "Places and Addresses",
            maxResults: 5,
            attribution: '<a href="https://developers.arcgis.com/en/features/geocoding/"></a>'
        }, suggestions: function (text, bounds, callback) {
            var request = this.suggest().text(text);
            if (bounds) {
                request.within(bounds)
            }
            if (this.options.countries) {
                request.countries(this.options.countries)
            }
            if (this.options.categories) {
                request.category(this.options.categories)
            }
            return request.run(function (error, results, response) {
                var suggestions = [];
                if (!error) {
                    while (response.suggestions.length && suggestions.length <= this.options.maxResults - 1) {
                        var suggestion = response.suggestions.shift();
                        if (!suggestion.isCollection) {
                            suggestions.push({text: suggestion.text, magicKey: suggestion.magicKey})
                        }
                    }
                }
                callback(error, suggestions)
            }, this)
        }, results: function (text, key, bounds, callback) {
            var request = this.geocode().text(text);
            if (key) {
                request.key(key)
            } else {
                request.maxLocations(this.options.maxResults)
            }
            if (bounds) {
                request.within(bounds)
            }
            if (this.options.forStorage) {
                request.forStorage(true)
            }
            return request.run(function (error, response) {
                callback(error, response.results)
            }, this)
        }
    });
    function arcgisOnlineProvider(options) {
        return new exports.ArcgisOnlineProvider(options)
    }

    exports.FeatureLayerProvider = esri_leaflet.FeatureLayerService.extend({
        options: {
            label: "Feature Layer",
            maxResults: 5,
            bufferRadius: 1e3,
            formatSuggestion: function (feature) {
                return feature.properties[this.options.searchFields[0]]
            }
        }, initialize: function (options) {
            esri_leaflet.FeatureLayerService.prototype.initialize.call(this, options);
            if (typeof this.options.searchFields === "string") {
                this.options.searchFields = [this.options.searchFields]
            }
        }, suggestions: function (text, bounds, callback) {
            var query = this.query().where(this._buildQuery(text)).returnGeometry(false);
            if (bounds) {
                query.intersects(bounds)
            }
            if (this.options.idField) {
                query.fields([this.options.idField].concat(this.options.searchFields))
            }
            var request = query.run(function (error, results, raw) {
                if (error) {
                    callback(error, [])
                } else {
                    this.options.idField = raw.objectIdFieldName;
                    var suggestions = [];
                    var count = Math.min(results.features.length, this.options.maxResults);
                    for (var i = 0; i < count; i++) {
                        var feature = results.features[i];
                        suggestions.push({
                            text: this.options.formatSuggestion.call(this, feature),
                            magicKey: feature.id
                        })
                    }
                    callback(error, suggestions.slice(0, this.options.maxResults).reverse())
                }
            }, this);
            return request
        }, results: function (text, key, bounds, callback) {
            var query = this.query();
            if (key) {
                query.featureIds([key])
            } else {
                query.where(this._buildQuery(text))
            }
            if (bounds) {
                query.within(bounds)
            }
            return query.run(L.Util.bind(function (error, features) {
                var results = [];
                for (var i = 0; i < features.features.length; i++) {
                    var feature = features.features[i];
                    if (feature) {
                        var bounds = this._featureBounds(feature);
                        var result = {
                            latlng: bounds.getCenter(),
                            bounds: bounds,
                            text: this.options.formatSuggestion.call(this, feature),
                            properties: feature.properties,
                            geojson: feature
                        };
                        results.push(result)
                    }
                }
                callback(error, results)
            }, this))
        }, _buildQuery: function (text) {
            var queryString = [];
            for (var i = this.options.searchFields.length - 1; i >= 0; i--) {
                var field = 'upper("' + this.options.searchFields[i] + '")';
                queryString.push(field + " LIKE upper('%" + text + "%')")
            }
            return queryString.join(" OR ")
        }, _featureBounds: function (feature) {
            var geojson = L.geoJson(feature);
            if (feature.geometry.type === "Point") {
                var center = geojson.getBounds().getCenter();
                var lngRadius = this.options.bufferRadius / 40075017 * 360 / Math.cos(180 / Math.PI * center.lat);
                var latRadius = this.options.bufferRadius / 40075017 * 360;
                return L.latLngBounds([center.lat - latRadius, center.lng - lngRadius], [center.lat + latRadius, center.lng + lngRadius])
            } else {
                return geojson.getBounds()
            }
        }
    });
    function featureLayerProvider(options) {
        return new exports.FeatureLayerProvider(options)
    }

    exports.MapServiceProvider = esri_leaflet.MapService.extend({
        options: {
            layers: [0],
            label: "Map Service",
            bufferRadius: 1e3,
            maxResults: 5,
            formatSuggestion: function (feature) {
                return feature.properties[feature.displayFieldName] + " <small>" + feature.layerName + "</small>"
            }
        }, initialize: function (options) {
            esri_leaflet.MapService.prototype.initialize.call(this, options);
            this._getIdFields()
        }, suggestions: function (text, bounds, callback) {
            var request = this.find().text(text).fields(this.options.searchFields).returnGeometry(false).layers(this.options.layers);
            return request.run(function (error, results, raw) {
                var suggestions = [];
                if (!error) {
                    var count = Math.min(this.options.maxResults, results.features.length);
                    raw.results = raw.results.reverse();
                    for (var i = 0; i < count; i++) {
                        var feature = results.features[i];
                        var result = raw.results[i];
                        var layer = result.layerId;
                        var idField = this._idFields[layer];
                        feature.layerId = layer;
                        feature.layerName = this._layerNames[layer];
                        feature.displayFieldName = this._displayFields[layer];
                        if (idField) {
                            suggestions.push({
                                text: this.options.formatSuggestion.call(this, feature),
                                magicKey: result.attributes[idField] + ":" + layer
                            })
                        }
                    }
                }
                callback(error, suggestions.reverse())
            }, this)
        }, results: function (text, key, bounds, callback) {
            var results = [];
            var request;
            if (key) {
                var featureId = key.split(":")[0];
                var layer = key.split(":")[1];
                request = this.query().layer(layer).featureIds(featureId)
            } else {
                request = this.find().text(text).fields(this.options.searchFields).contains(false).layers(this.options.layers)
            }
            return request.run(function (error, features, response) {
                if (!error) {
                    if (response.results) {
                        response.results = response.results.reverse()
                    }
                    for (var i = 0; i < features.features.length; i++) {
                        var feature = features.features[i];
                        layer = layer || response.results[i].layerId;
                        if (feature && layer !== undefined) {
                            var bounds = this._featureBounds(feature);
                            feature.layerId = layer;
                            feature.layerName = this._layerNames[layer];
                            feature.displayFieldName = this._displayFields[layer];
                            var result = {
                                latlng: bounds.getCenter(),
                                bounds: bounds,
                                text: this.options.formatSuggestion.call(this, feature),
                                properties: feature.properties,
                                geojson: feature
                            };
                            results.push(result)
                        }
                    }
                }
                callback(error, results.reverse())
            }, this)
        }, _featureBounds: function (feature) {
            var geojson = L.geoJson(feature);
            if (feature.geometry.type === "Point") {
                var center = geojson.getBounds().getCenter();
                var lngRadius = this.options.bufferRadius / 40075017 * 360 / Math.cos(180 / Math.PI * center.lat);
                var latRadius = this.options.bufferRadius / 40075017 * 360;
                return L.latLngBounds([center.lat - latRadius, center.lng - lngRadius], [center.lat + latRadius, center.lng + lngRadius])
            } else {
                return geojson.getBounds()
            }
        }, _layerMetadataCallback: function (layerid) {
            return L.Util.bind(function (error, metadata) {
                if (error) {
                    return
                }
                this._displayFields[layerid] = metadata.displayField;
                this._layerNames[layerid] = metadata.name;
                for (var i = 0; i < metadata.fields.length; i++) {
                    var field = metadata.fields[i];
                    if (field.type === "esriFieldTypeOID") {
                        this._idFields[layerid] = field.name;
                        break
                    }
                }
            }, this)
        }, _getIdFields: function () {
            this._idFields = {};
            this._displayFields = {};
            this._layerNames = {};
            for (var i = 0; i < this.options.layers.length; i++) {
                var layer = this.options.layers[i];
                this.get(layer, {}, this._layerMetadataCallback(layer))
            }
        }
    });
    function mapServiceProvider(options) {
        return new exports.MapServiceProvider(options)
    }

    exports.GeocodeServiceProvider = exports.GeocodeService.extend({
        options: {label: "Geocode Server", maxResults: 5},
        suggestions: function (text, bounds, callback) {
            if (this.options.supportsSuggest) {
                var request = this.suggest().text(text);
                if (bounds) {
                    request.within(bounds)
                }
                return request.run(function (error, results, response) {
                    var suggestions = [];
                    if (!error) {
                        while (response.suggestions.length && suggestions.length <= this.options.maxResults - 1) {
                            var suggestion = response.suggestions.shift();
                            if (!suggestion.isCollection) {
                                suggestions.push({text: suggestion.text, magicKey: suggestion.magicKey})
                            }
                        }
                    }
                    callback(error, suggestions)
                }, this)
            } else {
                callback(undefined, []);
                return false
            }
        },
        results: function (text, key, bounds, callback) {
            var request = this.geocode().text(text);
            request.maxLocations(this.options.maxResults);
            if (bounds) {
                request.within(bounds)
            }
            return request.run(function (error, response) {
                callback(error, response.results)
            }, this)
        }
    });
    function geocodeServiceProvider(options) {
        return new exports.GeocodeServiceProvider(options)
    }

    exports.VERSION = "2.0.3";
    exports.WorldGeocodingServiceUrl = (window.location.protocol === "https:" ? "https:" : "http:") + "//geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer/";
    exports.geocode = geocode;
    exports.reverseGeocode = reverseGeocode;
    exports.suggest = suggest;
    exports.geocodeService = geocodeService;
    exports.geosearch = geosearch;
    exports.arcgisOnlineProvider = arcgisOnlineProvider;
    exports.featureLayerProvider = featureLayerProvider;
    exports.mapServiceProvider = mapServiceProvider;
    exports.geocodeServiceProvider = geocodeServiceProvider
});
//# sourceMappingURL=./esri-leaflet-geocoder.js.map
$("#geocoder-control-input").on("click", function () {
    $(".container").css("display", "show")
});