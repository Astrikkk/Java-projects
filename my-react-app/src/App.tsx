import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import CategoryCreatePage from './components/category/create'
import CategoryViewPage from './components/category/view'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <CategoryViewPage />
    </>
  )
}

export default App
