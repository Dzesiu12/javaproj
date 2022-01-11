import ClientDashboard from './pages/ClientDashboard';
import StaffDashboard from './pages/StaffDashboard';
import ManagerDashboard from './pages/ManagerDashboard';
import { useAuth } from '../../providers/AuthProvider';
import { Role } from '../../enums/Role.enum';
import { Button } from 'baseui/button';
import { RiLogoutBoxRLine } from 'react-icons/ri';

const DashboardPage = () => {
  const { roles, logout, username } = useAuth();

  return (
    <div className='py-24'>
      <header className='flex items-center justify-between py-4 border-b border-white'>
        <h1 className='text-2xl font-bold'>Hello {username}</h1>

        <Button
          onClick={logout}
          type='button'
          size='compact'
        >
          <span>Logout</span>
          <RiLogoutBoxRLine className='ml-2' />
        </Button>
      </header>

      <div className='py-6'>
        {roles.includes(Role.CLIENT) && <ClientDashboard />}
        {roles.includes(Role.STAFF) && <StaffDashboard />}
        {roles.includes(Role.MANAGER) && <ManagerDashboard />}
      </div>
    </div>
  );
};

export default DashboardPage;
