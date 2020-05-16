(function() {
  var INPUT_TYPES_CLICK = [
    'submit',
    'reset',
    'button',
    'image'
  ];
  var INPUT_TYPES_TEXT = [
    'text',
    'password',
    'search',
    'tel',
    'url',
    'email',
    'datetime',
    'date',
    'month',
    'week',
    'time',
    'datetime-local',
    'number',
    'range',
    'color'
  ];
  var INPUT_TYPES_SELECT = [
    'checkbox',
    'radio'
  ];
  var INPUT_TYPES_CHANGE = INPUT_TYPES_TEXT.concat(INPUT_TYPES_SELECT);

  var elements = [];
  var clickedTimeStamps = {};
  var changedTimeStamps = {};

  document.addEventListener('click', onClickListener);
  document.addEventListener('change', onChangeListener);
  window.addEventListener("hashchange", resetAllListeners);
  window.addEventListener("popstate", resetAllListeners);
  listenPushState();
  addAllListeners();

  function listenPushState() {
    var oldPushState = window.history.pushState;
    window.history.pushState = function(state) {
      oldPushState.apply(window.history, arguments);
      resetAllListeners();
    };
  }

  function addAllListeners() {
    document.querySelectorAll('*').forEach(function(element) {
      element.addEventListener('click', onClickListener);
      element.addEventListener('change', onChangeListener);
      elements.push(element);
    });
  }

  function removeAllListeners() {
    elements.forEach(function(element) {
      element.removeEventListener('click', onClickListener);
      element.removeEventListener('change', onChangeListener);
    });
    elements = [];
  }

  function resetAllListeners() {
    removeAllListeners();
    addAllListeners();
  }

  function onClickListener(event) {
    if (clickedTimeStamps[event.timeStamp]) return;
    var target = event.target;
    var type = target.type;
    var tag = target.localName.toLowerCase();
    if (tag === 'textarea') return;
    if (tag === 'select') return;
    if (tag === 'input' && INPUT_TYPES_CLICK.indexOf(type) === -1) return;
    var targetType = getTargetType(target);
    if (!targetType) return;
    var xPath = getXpath(target);
    var value = target.innerText || target.href || target.src || '';
    WebsiteMacroRecorder.onClick(xPath, targetType, value);
    clickedTimeStamps = {};
    clickedTimeStamps[event.timeStamp] = true;
  }

  function onChangeListener(event) {
    if (changedTimeStamps[event.timeStamp]) return;
    var target = event.target;
    var type = target.type;
    var tag = target.localName.toLowerCase();
    if (tag !== 'input' && tag !== 'textarea' && tag !== 'select') return;
    if (tag === 'input' && INPUT_TYPES_CHANGE.indexOf(type) === -1) return;
    var targetType = getTargetType(target);
    if (!targetType) return;
    var xPath = getXpath(target);
    var value = target.value;
    if (tag === 'textarea' || INPUT_TYPES_TEXT.indexOf(type) >= 0) {
      WebsiteMacroRecorder.onChange(xPath, targetType, value);
    } else {
      WebsiteMacroRecorder.onSelect(xPath, targetType, value);
    }
    changedTimeStamps = {};
    changedTimeStamps[event.timeStamp] = true;
  }

  function escape(str) {
    return str.replace(/\\/g, '\\\\').replace(/'/g, '\\\'');
  }

  function $x(path, element) {
    element = element || document;
    var result = document.evaluate(path, element, null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
    var item = result.iterateNext();
    var items = [];
    while (item) {
      items.push(item);
      item = result.iterateNext();
    }
    return items;
  }

  function getXpath(element, path) {
    path = path || '';
    if (!element) {
      return '/' + path;
    }
    if (path) {
      var relativePath = '//' + path;
      var unique = $x(relativePath, document).length === 1;
      if (unique) return relativePath;
    }
    var currentPath = element.localName.toLowerCase();
    if (element.id) {
      currentPath += "[@id='" + escape(element.id) + "']";
    }
    if (element.className) {
      currentPath += "[@class='" + escape(element.className) + "']";
    }
    if (element.type) {
      currentPath += "[@type='" + escape(element.type) + "']";
    }
    if (element.name) {
      currentPath += "[@name='" + escape(element.name) + "']";
    }
    var siblings = $x(currentPath, element.parentElement);
    if (siblings.length >= 2) {
      var index = Array.prototype.indexOf.call(siblings, element) + 1;
      currentPath += '[' + index + ']';
    }
    if (path) {
      path = currentPath + '/' + path;
    } else {
      path = currentPath;
    }
    return getXpath(element.parentElement, path);
  }

  function getTargetType(element) {
    var tag = element.localName.toLowerCase();
    var type = element.type;
    switch (tag) {
      case 'video':
      case 'canvas':
        return null;
      case 'img':
      case 'area':
        return 'image';
      case 'a':
        return 'link';
      case 'iframe':
        return 'frame';
      case 'textarea':
        return 'input';
      case 'input':
        switch (type) {
          case 'checkbox':
            return 'checkbox';
          case 'radio':
            return 'radio';
          case 'submit', 'reset', 'button', 'image':
            return 'button';
          default:
            return 'input';
        }
      case 'button':
        return 'button';
      case 'select':
        return 'selectbox';
      default:
        return 'text';
    }
  }
})();
