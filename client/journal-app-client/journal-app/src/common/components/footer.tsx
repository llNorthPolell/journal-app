import Link from 'next/link';

function Footer(){
    return (
        <>
            <footer className="footer">
                <ul className="footer__nav">
                <li className="footer__nav-item"><Link href="#" className="footer__nav-link">Contact Us</Link></li>
                <li className="footer__nav-item"><Link href="#" className="footer__nav-link">Feedback</Link></li>
                <li className="footer__nav-item"><p className="footer__legal">&copy;2023 llnorthpolell</p></li>
            </ul>
        </footer>
      </>
    )
}

export default Footer;