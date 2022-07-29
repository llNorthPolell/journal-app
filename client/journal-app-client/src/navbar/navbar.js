import {useAuth} from '../contexts/authContext';
import {Link} from 'react-router-dom';
import {useNavigate} from 'react-router-dom';

function Navbar(props){
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