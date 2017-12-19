Hello,

$displayName$ has submitted a storage request via the Research Hub. Please find the details below.

General
======================
Researcher:
    Name: $displayName$
    Username: $requestorUpi$
    Email: $mail$
Storage type: $storageType$

Project
======================
Title: $title$
Abstract: $abstract$
End date: $endDate$
Fields of research: $fieldOfResearch:{n|$\n$$\ $$\ $$\ $$\ $$n$}$

Data Info
======================
Short name: $shortName$
Data needs: $dataRequirements:{n|$\n$$\ $$\ $$\ $$\ $$n$}$$if(dataRequirementsOther)$: $dataRequirementsOther$$endif$
Project members: $projectMembers:{n|$\n$$\ $$\ $$\ $$\ $$n.firstName$ $n.lastName$, $n.email$, $n.username$, $n.access$, $n.roles:{r|$r$ }$}$

Data Size
======================
This year: $sizeThisYear$ $unitThisYear$
Next year: $sizeNextYear$ $unitNextYear$
Comments: $comments$

Thanks.
