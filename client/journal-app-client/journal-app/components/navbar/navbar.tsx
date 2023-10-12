import { getServerSession } from 'next-auth';
import Link from 'next/link';
import { authOptions } from '../../app/api/auth/[...nextauth]/route';
import LogoutBtn from './logoutBtn';

export default async function Navbar(){
    const session = await getServerSession(authOptions);

    return (
        <nav className="navbar">
            <Link className="navbar__logo" href="/">
                <span>Journal App</span>
            </Link>

            {
                    <div className="navbar__journal-links">
                        <div className="navbar__item">
                            <Link href="/" className="navbar__link">
                                Home
                            </Link>
                        </div>
                        <div className="navbar__item">
                            <Link href="#" className="navbar__link">
                                About
                            </Link>
                        </div>
                        <div className="navbar__item">
                            <Link href="#" className="navbar__link">
                                Contact
                            </Link>
                        </div>
                        <div className="navbar__item">
                            <Link href="#" className="navbar__link">
                                Support
                            </Link>
                        </div>
                    </div>

            }

            <div className="navbar__item">
                {

                    (session?.user) ?
                        <LogoutBtn />
                        :
                        <Link className="navbar__link" href="/login">Login</Link>
                }
            </div>

        </nav>
    );
}