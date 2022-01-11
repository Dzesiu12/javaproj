import { createContext, PropsWithChildren, useContext, useState } from 'react';
import { toast } from 'react-toastify';
import { Role } from '../enums/Role.enum';

const AuthContext = createContext<any>({});

const AuthProvider = ({ children }: PropsWithChildren<{}>) => {
  const [username, setUsername] = useState<string | any>(null);
  const [roles, setRoles] = useState<Role[] | null>(null);
  const [authToken, setAuthToken] = useState<string | null>(() => localStorage.getItem('authToken') ?? null);

  const authWithCredentials = async (endpoint: string, credentials: any) => {
    if (authToken) return;

    const res = await fetch(endpoint, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(credentials),
    });

    const { responseType, message, data } = await res.json();

    if (responseType === 'SUCCESS') {
      toast.success(message);
      setUsername(data.username);
      setRoles(data.roles);
      setAuthToken(data.token);
    } else {
      toast.error(message);
    }
  };

  const login = async (credentials: any) => {
    return await authWithCredentials('http://localhost:8080/api/auth/login', credentials);
  };

  const register = async (credentials: any) => {
    return await authWithCredentials('http://localhost:8080/api/auth/register', credentials);
  };

  const logout = () => {
    setUsername(null);
    setRoles(null);
    setAuthToken(null);
  }

  const value = {
    username,
    roles,
    authToken,
    login,
    register,
    logout,
    isAuthenticated: !!authToken,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('Unable to use AuthContext outside AuthProvider');
  }

  return context;
};

export default AuthProvider;
