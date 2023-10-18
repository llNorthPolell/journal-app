'use client'
import Link from 'next/link';
import LogoutBtn from './logoutBtn';
import { useSession } from 'next-auth/react';
import { useParams } from 'next/navigation';

export default function Navbar() {
    const { status } = useSession();
    const {journalSeg} = useParams();

    return (
        <nav className="navbar">
            <Link className="navbar__logo" href="/">
                <span>Journal App</span>
            </Link>


            <div className="navbar__journal-links">
                {
                    (journalSeg && journalSeg[0]) ?
                        <>
                            <div className="navbar__item">
                                <Link href={`/${journalSeg[0]}`} className="navbar__link">
                                    Dashboard
                                </Link>
                            </div>
                            <div className="navbar__item">
                                <Link href={`/${journalSeg[0]}/entries`} className="navbar__link">
                                    Entries
                                </Link>
                            </div>
                            <div className="navbar__item">
                                <Link href="#" className="navbar__link">
                                    Goals
                                </Link>
                            </div>
                            <div className="navbar__item">
                                <Link href="#" className="navbar__link">
                                    Settings
                                </Link>
                            </div>
                            <div className="navbar__item">
                                <Link href="#" className="navbar__link">
                                    Support
                                </Link>
                            </div>
                        </>
                        :
                        <>
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
                        </>
                }
            </div>




            <div className="navbar__item">
                {

                    (status === "authenticated") ?
                        <LogoutBtn />
                        :
                        <Link className="navbar__link" href="/login">Login</Link>
                }
            </div>

        </nav>
    );
}