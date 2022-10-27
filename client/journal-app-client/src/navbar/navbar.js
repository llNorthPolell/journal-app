import {useAuth} from '../contexts/authContext';
import {Link, useParams} from 'react-router-dom';
import {useNavigate} from 'react-router-dom';

function Navbar(props){
    const { journalId } = useParams();
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    //todo
    const handleLogout = (e) =>{
        e.preventDefault();
        logout();
        navigate('/');
    }

    return (
        <nav id="Navbar" className="navbar fixed-top navbar-expand-lg bg-dark">
            <div className="container-fluid">
                <Link className="navbar-brand" to="/">
                    <span>Journal App</span>
                </Link>
                <div className="collapse navbar-collapse">
                    {
                        (user && journalId)?
                        <>
                            <li className="nav-item">
                                <Link to="/" className="nav-link text-center text-light">
                                    Home
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link to={"/"+journalId} className="nav-link text-center text-light">
                                    Dashboard
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link to={"/"+journalId+"/goals"} className="nav-link text-center text-light">
                                    Goals
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link to={"/"+journalId+"/entries"} className="nav-link text-center text-light">
                                    Journal Entries
                                </Link>
                            </li>
                        </>
                        :
                        <></>

                    }
                </div>

                {
                    (user == null)?   
                        <Link className="nav-link navText" to="/login">Login</Link>
                    :
                        <button className="btn btn-link navText" onClick={handleLogout}>Logout</button> 
                }
            </div>
        </nav>
    );
}
export default Navbar;