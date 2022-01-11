import { Card } from 'baseui/card';
import { Button } from 'baseui/button';
import { AnimateSharedLayout, motion } from 'framer-motion';
import { useAuth } from '../../../providers/AuthProvider';
import { useEffect, useState } from 'react';
import { useInterval } from 'react-use';
import { MdDone } from 'react-icons/md';
import { Tag } from 'baseui/tag';
import { toast } from 'react-toastify';

const StaffDashboard = () => {
  const { authToken } = useAuth();

  const [orders, setOrders] = useState<any[]>([]);

  const fetchOrders = async () => {
    const res = await fetch('http://localhost:8080/api/orders/staff', {
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    });

    const { data } = await res.json();
    setOrders(data.orders);
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  useInterval(fetchOrders, 5000);

  const handleMarkAsCompleted = async (orderId: string) => {
    const res = await fetch(`http://localhost:8080/api/orders/staff/complete/${orderId}`, {
      method: 'PUT',
      headers: {
        Authorization: `Bearer ${authToken}`,
        'Content-Type': 'application/json',
      },
    });

    const { responseType, message } = await res.json();
    if (responseType === 'SUCCESS') {
      toast.success(message);
      fetchOrders();
    } else {
      toast.error(message);
    }
  };

  return (
    <div>
      <h2 className='text-2xl font-bold mb-4'>Orders</h2>

      <AnimateSharedLayout>
        <ul className='flex flex-col space-y-2'>
          {orders.sort((a, b) => a.isCompleted - b.isCompleted).map((order: any) => (
            <motion.li
              layout
              key={order.id}
              className='flex flex-col'
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
            >
              <Card>
                <div className='flex'>
                  <div>
                    {order.isCompleted ? (
                      <Tag kind='green' closeable={false}>Done</Tag>
                    ) : (
                      <Tag kind='yellow' closeable={false}>Waiting</Tag>
                    )}

                    <div className='mt-2'>
                      <ul className='flex flex-col space-y-2'>
                        {order.dishes.map(({ id, name, quantity }: any) => (
                          <li key={id}>{quantity}x <span className="font-bold">{name}</span></li>
                        ))}
                      </ul>
                    </div>
                  </div>

                  <div className='ml-auto flex space-x-2'>
                    {!order.isCompleted && (
                      <Button
                        type='button'
                        size='compact'
                        kind='secondary'
                        shape='circle'
                        onClick={() => handleMarkAsCompleted(order.id)}
                      >
                        <MdDone />
                      </Button>
                    )}
                  </div>
                </div>
              </Card>
            </motion.li>
          ))}
        </ul>
      </AnimateSharedLayout>
    </div>
  );
};

export default StaffDashboard;
