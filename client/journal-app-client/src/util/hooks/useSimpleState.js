import {useState} from 'react'



const useSimpleState = initValue => {
    const [value, setValue] = useState(initValue);
    const handleUpdate = e => {
        setValue(e.target.value);
    }
    const overrideValue = newValue => {
        setValue(newValue);
    }
    return [value, overrideValue, handleUpdate];
}

export default useSimpleState;
