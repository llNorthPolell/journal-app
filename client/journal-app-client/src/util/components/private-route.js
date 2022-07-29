import {Navigate} from "react-router-dom";
import {useAuth} from "../../contexts/authContext";

function PrivateRoute({children}){
    const {user} = useAuth();

    if (user ==null){
        return (
            <Navigate to="/login"></Navigate>
        );
    }
    else {
        return children; 
    }
}
export default PrivateRoute;