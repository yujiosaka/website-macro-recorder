(function() {
  var MAX_CLICK_VALUE_LENGTH = 10;
  var MAX_ID_LENGTH = 10;
  var MAX_CLASS_LENGTH = 10;
  var MAX_NAME_LENGTH = 10;
  var MAX_TYPE_LENGTH = 10;
  var MAX_HREF_LENGTH = 20;
  var MAX_SRC_LENGTH = 20;
  var MAX_ALT_LENGTH = 20;
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

  document.addEventListener('click', function(event) {
    var target = event.target;
    var type = target.type;
    var tag = target.localName.toLowerCase();
    if (tag === 'textarea') return;
    if (tag === 'input' && INPUT_TYPES_CLICK.indexOf(type) === -1) return;
    var value = truncate(target.innerText, MAX_CLICK_VALUE_LENGTH);
    var xPath = getXpath(target);
    var cssSelector = getCssSelector(target);
    WebsiteMacroRecorder.onClick(value, xPath, cssSelector);
  });

  document.addEventListener('change', function(event) {
    var target = event.target;
    var type = target.type;
    var tag = target.localName.toLowerCase();
    if (tag !== 'input' && tag !== 'textarea') return;
    if (tag === 'input' && INPUT_TYPES_CHANGE.indexOf(type) === -1) return;
    var value = target.value;
    var xPath = getXpath(target);
    var cssSelector = getCssSelector(target);
    if (tag === 'textarea' || INPUT_TYPES_TEXT.indexOf(type) >= 0) {
      WebsiteMacroRecorder.onChange(value, xPath, cssSelector);
    } else {
      WebsiteMacroRecorder.onSelect(value, xPath, cssSelector);
    }
  });

  function truncate(str, length) {
    str = str || '';
    return ( str.length > length )
           ? str.substr(0, length - 1) + '…'
           : str;
  }

  function escape(str) {
    return str.replace(/\\/g, '\\\\').replace(/'/g, '\\\'');
  }

  function $x(path, element) {
    element = element || document;
    var result = document.evaluate(path, element, null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
    var node = result.iterateNext();
    var nodes = [];
    while (node) {
      nodes.push(node);
      node = result.iterateNext();
    }
    return nodes;
  }

  function getXpath(node, path) {
    path = path || '';
    if (!node) {
      return '/' + path;
    }
    if (path) {
      var relativePath = '//' + path;
      var unique = $x(relativePath, document).length === 1;
      if (unique) return relativePath;
    }
    var currentPath = node.localName.toLowerCase();
    if (node.id) {
      currentPath += "[@id='" + escape(node.id) + "']";
    }
    if (node.className) {
      currentPath += "[@class='" + escape(node.className) + "']";
    }
    if (node.type) {
      currentPath += "[@type='" + escape(node.type) + "']";
    }
    if (node.name) {
      currentPath += "[@name='" + escape(node.name) + "']";
    }
    var siblings = $x(currentPath, node.parentElement);
    if (siblings.length >= 2) {
      var index = Array.prototype.indexOf.call(siblings, node) + 1;
      currentPath += '[' + index + ']';
    }
    if (path) {
      path = currentPath + '/' + path;
    } else {
      path = currentPath;
    }
    return getXpath(node.parentElement, path);
  }

  function getCssSelector(node, path) {
    var selector = node.localName.toLowerCase();
    if (node.id) selector += "[id='" + escape(node.id) + "']";
    if (node.className) selector += "[class='" + escape(truncate(node.className, MAX_CLASS_LENGTH)) + "']";
    if (node.type) selector += "[type='" + escape(truncate(node.type, MAX_TYPE_LENGTH)) + "']";
    if (node.name) selector += "[name='" + escape(truncate(node.name, MAX_NAME_LENGTH)) + "']";
    if (node.href) selector += "[href='" + escape(truncate(node.href, MAX_HREF_LENGTH)) + "']";
    if (node.src) selector += "[src='" + escape(truncate(node.src, MAX_SRC_LENGTH)) + "']";
    if (node.alt) selector += "[alt='" + escape(truncate(node.alt, MAX_ALT_LENGTH)) + "']";
    return selector;
  }
})();
