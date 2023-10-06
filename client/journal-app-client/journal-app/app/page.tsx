import { getServerSession } from 'next-auth'
import Landing from '../components/home/landing'
import { authOptions } from './api/auth/[...nextauth]/route';
import Journals from '../components/home/journals';



export default async function Home() {
  const session = await getServerSession(authOptions);

  return (
    <>
      {
        (session?.user)?
          <Journals/>      
          :
          <Landing />
      }
    </>
  )
}
