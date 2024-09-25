import { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import reactLogo from './assets/react.svg';
import viteLogo from '/vite.svg';
import './App.css';
import CategoryCreatePage from './components/category/create';
import CategoryViewPage from './components/category/view';
import ProductViewPage from './components/product/view';
import ProductCreatePage from './components/product/view';

function App() {
  const [count, setCount] = useState(0);

  return (
    <Router>
      <Routes>
        <Route path="/category/view" element={<CategoryViewPage />} />
        <Route path="/category/create" element={<CategoryCreatePage />} />
        <Route path="/product/view" element={<ProductViewPage />} />
        <Route path="/product/create" element={<ProductCreatePage />} />
      </Routes>
    </Router>
  );
}

export default App;
