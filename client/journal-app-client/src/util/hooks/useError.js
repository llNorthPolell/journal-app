import {useState} from 'react'



const useError = () => {
    const [errorList,setErrorList] = useState([]);
    const throwError = (message,errLocation) =>{   
        const error = {
            message: message,
            location: errLocation
        }
        
        if(!errorList.some(err=>err.message===error.message && JSON.stringify(err.location)===JSON.stringify(error.location)))
            setErrorList([...errorList,error]);

        console.error("ERROR: " + error.message + " at "+ JSON.stringify(errLocation));
        return error;
    }

    const clear = (location)=>{
        if (location){
            setErrorList(errorList.filter(error=> !error.location.includes(location)));
            return;
        }

        setErrorList([]);
    }
    
    return [errorList,throwError,clear];
}

export default useError;
