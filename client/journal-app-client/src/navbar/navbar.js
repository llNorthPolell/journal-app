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
        navigate('/journal-app/');
    }

    return (
        <div id="Navbar" className="navbar navbar-expand-lg bg-dark">
            <div className="container-fluid">
                <Link className="navbar-brand" to="/journal-app/">
                    <span>Journal App</span>
                </Link>
                <div className="collapse navbar-collapse">
                </div>

                {
                    (user == null)?   
                        <Link className="nav-link text-light" to="/journal-app/login">Login</Link>
                    :
                        <button className="btn btn-link text-light" onClick={handleLogout}>Logout</button> 
                }
            </div>
        </div>
    );
}
export default Navbar;