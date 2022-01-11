import { Card } from 'baseui/card';
import { useState } from 'react';
import { useAuth } from '../../providers/AuthProvider';
import AuthForm from '../../components/AuthForm';

const AuthPage = () => {
  const auth = useAuth();
  const [isAuthLoading, setIsAuthLoading] = useState<boolean>(false);

  const handleLogin = (data: any) => {
    setIsAuthLoading(true);
    auth.login(data);
    setIsAuthLoading(false);
  };

  const handleRegister = (data: any) => {
    setIsAuthLoading(true);
    auth.register(data);
    setIsAuthLoading(false);
  };

  return (
    <div className='py-24'>
      <div className='grid grid-cols-2 gap-6'>
        <Card>
          <div className='p-4'>
            <h1 className='font-bold text-xl'>Login</h1>
            <AuthForm
              disabled={isAuthLoading}
              submitLabel='Login'
              onSubmit={handleLogin}
            />
          </div>
        </Card>

        <Card>
          <div className='p-4'>
            <h1 className='font-bold text-xl'>Register</h1>
            <AuthForm
              disabled={isAuthLoading}
              submitLabel='Register'
              onSubmit={handleRegister}
            />
          </div>
        </Card>
      </div>
    </div>
  );
};

export default AuthPage;
