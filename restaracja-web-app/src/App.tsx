import { ToastContainer } from 'react-toastify';
import { useAuth } from './providers/AuthProvider';
import AuthPage from './pages/AuthPage/AuthPage';
import DashboardPage from './pages/DashboardPage/DashboardPage';

const App = () => {
  const auth = useAuth();

  return (
    <div className='container mx-auto px-8'>
      {auth.isAuthenticated ? <DashboardPage /> : <AuthPage />}

      <ToastContainer
        position='top-right'
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
    </div>
  );
};

export default App;
