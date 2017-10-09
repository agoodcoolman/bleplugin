//加载cordova对象以及exec方法
var exec = require('cordova/exec'),
    cordova = require('cordova');

//定义插件对象
var HDOITBLEPlugin = function() {};
//定义接口，面向前端angularjs代码的接口，接口参数三个
HDOITBLEPlugin.prototype.hdoitBleExecute = function(successCallback, errorCallback, execFun, options) {
    if (successCallback == null) {
        successCallback = function() {}
    }
    if (errorCallback == null) {
        errorCallback = function() {}
    }

    if (typeof errorCallback != "function") {
        console.log("HDOITBLEPlugin.hdoitBleExecute: failure: failure parameter not a function");
        return
    }

    if (typeof successCallback != "function") {
        console.log("HDOITBLEPlugin.hdoitBleExecute: success callback parameter must be a function");
        return
    }

    if (options instanceof Array) {
        cordova.exec(successCallback, errorCallback, 'bleplugin', execFun, options);
    } else {
        //调用原生代码，
        cordova.exec(successCallback, errorCallback, 'bleplugin', execFun, [options]);
    }
};


HDOITBLEPlugin.prototype.init = function(successCallback, errorCallback, options) {
  console.log("plugin init");
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'bleInit', [options]);
};

HDOITBLEPlugin.prototype.scan = function(successCallback, errorCallback, options) {
    console.log("plugin scan");
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'scan', [options]);
};

HDOITBLEPlugin.prototype.close = function(successCallback, errorCallback, options) {
  console.log("plugin close");
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'close', [options]);
};

HDOITBLEPlugin.prototype.isEnabled = function(successCallback, errorCallback, options) {
  console.log("plugin isEnabled");
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'isEnabled', [options]);
};

HDOITBLEPlugin.prototype.reset = function(successCallback, errorCallback, options) {
  console.log("plugin reset");
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'reset', [options]);
};

HDOITBLEPlugin.prototype.doPrint = function(successCallback, errorCallback, options) {
  console.log("plugin doPrint");
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'doPrint', [options]);
};

HDOITBLEPlugin.prototype.printTextZhike = function(successCallback, errorCallback, printText, options) {
  console.log("plugin printTextZhike");
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'printTextZhike', [printText, options]);
};

HDOITBLEPlugin.prototype.printBarcodeZhike = function(successCallback, errorCallback, printText, options) {
  console.log("plugin printBarcodeZhike");
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'printBarcodeZhike', [printText, options]);
};

HDOITBLEPlugin.prototype.printQRcodeZhike = function(successCallback, errorCallback, printText, options) {
  console.log("plugin printQRcodeZhike");
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'printQRcodeZhike', [printText, options]);
};

HDOITBLEPlugin.prototype.startPageZhike = function(successCallback, errorCallback, options) {
  console.log("plugin startPageZhike");
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'startPageZhike', [options]);
};

HDOITBLEPlugin.prototype.darwRectZhike = function(successCallback, errorCallback, options) {
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'darwRectZhike', [options]);
};

HDOITBLEPlugin.prototype.darwLineZhike = function(successCallback, errorCallback, options) {
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'darwLineZhike', [options]);
};
HDOITBLEPlugin.prototype.bluetoothName = function(successCallback, errorCallback, options) {
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'bluetoothName', [options]);
};
HDOITBLEPlugin.prototype.gotoMarklabel = function(successCallback, errorCallback, options) {
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'gotoMarklabel', [options]);
};
HDOITBLEPlugin.prototype.multilineText = function(successCallback, errorCallback, printText,options) {
    cordova.exec(successCallback, errorCallback, 'bleplugin', 'multilineText', [printText, options]);
};

// 把新的插件对象对象，赋值给module.exports属性。
var hdoitbleplugin = new HDOITBLEPlugin();
module.exports = hdoitbleplugin;
