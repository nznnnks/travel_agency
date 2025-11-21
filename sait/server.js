const http = require('http');
const https = require('https');
const fs = require('fs');
const path = require('path');
const { URL } = require('url');

const PORT = process.env.PORT || 3000;
console.log(`Environment PORT: ${process.env.PORT}`);
console.log(`Using PORT: ${PORT}`);
const MIME_TYPES = {
  '.html': 'text/html',
  '.js': 'application/javascript',
  '.css': 'text/css',
  '.json': 'application/json',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.gif': 'image/gif',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon'
};

// Функция для добавления CORS заголовков
function addCorsHeaders(res) {
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS, PATCH');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization, X-Requested-With, Accept');
  res.setHeader('Access-Control-Max-Age', '3600');
  res.setHeader('Access-Control-Expose-Headers', 'Authorization, Content-Type');
}

// Функция для проксирования запросов к API
function proxyApiRequest(req, res) {
  const url = new URL(req.url, `http://${req.headers.host}`);
  const apiPath = url.pathname;
  
  // Определяем целевой API URL
  let targetUrl;
  if (apiPath.startsWith('/api/users')) {
    targetUrl = 'https://responsible-education-production.up.railway.app' + apiPath;
  } else if (apiPath.startsWith('/api/trips')) {
    targetUrl = 'https://terrific-purpose-production.up.railway.app' + apiPath;
  } else if (apiPath.startsWith('/api/reviews')) {
    targetUrl = 'https://gentle-benevolence-production.up.railway.app' + apiPath;
  } else if (apiPath.startsWith('/api/matches')) {
    targetUrl = 'https://positive-nature-production.up.railway.app' + apiPath;
  } else {
    res.writeHead(404, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ error: 'API endpoint not found' }), 'utf-8');
    return;
  }

  console.log(`Proxying ${req.method} ${apiPath} to ${targetUrl}`);

  // Обработка OPTIONS запроса (preflight)
  if (req.method === 'OPTIONS') {
    addCorsHeaders(res);
    res.writeHead(200);
    res.end();
    return;
  }

  // Создаем опции для запроса
  const urlObj = new URL(targetUrl);
  const options = {
    hostname: urlObj.hostname,
    port: urlObj.port || (urlObj.protocol === 'https:' ? 443 : 80),
    path: urlObj.pathname + urlObj.search,
    method: req.method,
    headers: {
      'Content-Type': req.headers['content-type'] || 'application/json',
      'Accept': req.headers['accept'] || 'application/json',
    }
  };

  // Добавляем Authorization заголовок, если есть
  if (req.headers['authorization']) {
    options.headers['Authorization'] = req.headers['authorization'];
  }

  // Выбираем модуль (http или https)
  const client = urlObj.protocol === 'https:' ? https : http;

  // Создаем запрос к целевому API
  const proxyReq = client.request(options, (proxyRes) => {
    // Добавляем CORS заголовки к ответу
    addCorsHeaders(res);
    
    // Копируем статус код
    res.writeHead(proxyRes.statusCode, proxyRes.headers);
    
    // Передаем данные
    proxyRes.pipe(res);
  });

  // Обработка ошибок
  proxyReq.on('error', (error) => {
    console.error(`Proxy error for ${targetUrl}:`, error);
    addCorsHeaders(res);
    res.writeHead(502, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ 
      error: 'Bad Gateway', 
      message: 'Failed to connect to API server',
      details: error.message 
    }), 'utf-8');
  });

  // Передаем тело запроса, если есть
  req.pipe(proxyReq);
}

const server = http.createServer((req, res) => {
  console.log(`Request: ${req.method} ${req.url}`);
  
  // Если это запрос к API, проксируем его
  if (req.url.startsWith('/api/')) {
    proxyApiRequest(req, res);
    return;
  }

  // Иначе обслуживаем статические файлы
  let filePath = '.' + req.url;
  if (filePath === './') {
    filePath = './index.html';
  }

  const extname = String(path.extname(filePath)).toLowerCase();
  const contentType = MIME_TYPES[extname] || 'application/octet-stream';

  console.log(`Serving file: ${filePath}`);
  fs.readFile(filePath, (error, content) => {
    if (error) {
      console.error(`Error reading file ${filePath}:`, error);
      if (error.code === 'ENOENT') {
        addCorsHeaders(res);
        res.writeHead(404, { 'Content-Type': 'text/html' });
        res.end('<h1>404 - File Not Found</h1>', 'utf-8');
      } else {
        addCorsHeaders(res);
        res.writeHead(500);
        res.end(`Server Error: ${error.code}`, 'utf-8');
      }
    } else {
      console.log(`Successfully served: ${filePath}`);
      addCorsHeaders(res);
      res.writeHead(200, { 'Content-Type': contentType });
      res.end(content, 'utf-8');
    }
  });
});

// Проверка доступных файлов при старте
fs.readdir('.', (err, files) => {
  if (err) {
    console.error('Error reading directory:', err);
  } else {
    console.log('Available files in directory:', files);
  }
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`Server running at http://0.0.0.0:${PORT}/`);
  console.log(`Server accessible at http://localhost:${PORT}/`);
});

