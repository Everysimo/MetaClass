import {checkRole} from "../../../functions/checkRole";

export const checkOrg = async (id_stanza) => {
    const fetchedRole = await checkRole(id_stanza);
    if(fetchedRole === "Organizzatore" || fetchedRole === "Organizzatore_Master")
        return true;
    else
        return false;
}