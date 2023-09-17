import Link from 'next/link';
import {useRouter} from 'next/router';
import { useAuth } from '../context/authContext';

function Navbar(){
    const router = useRouter();
    const {user,logout} = useAuth();
    const journalId = router.query.journalId;

    const handleLogout = async ()=>{
        console.log("LOGOUT");
        logout();
        router.push("/login");
    };

    return (
        <nav className="navbar">
            <Link className="navbar__logo" href="/">
                <span>Journal App</span>
            </Link>

            {
                (user && journalId) ?
                    <div className="navbar__journal-links">
                        <div className="navbar__item">
                            <Link href="/" className="navbar__link">
                                Home
                            </Link>
                        </div>
                        <div className="navbar__item">
                            <Link href={"/" + journalId} className="navbar__link">
                                Dashboard
                            </Link>
                        </div>
                        <div className="navbar__item">
                            <Link href={"/" + journalId + "/entries"} className="navbar__link">
                                Entries
                            </Link>
                        </div>
                        <div className="navbar__item">
                            <Link href={"/" + journalId + "/goals"} className="navbar__link">
                                Goals
                            </Link>
                        </div>
                        <div className="navbar__item">
                            <Link href="#" className="navbar__link">
                                Support
                            </Link>
                        </div>
                    </div>
                    :
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

                    (user) ?
                        <button className="navbar__btn-link" onClick={handleLogout}>Logout</button>
                        :
                        <Link className="navbar__link" href="/login">Login</Link>
                }
            </div>

        </nav>
    );
}
export default Navbar;