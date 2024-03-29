# Model-Driven Engineering (MDE) approach to generate Machine Learning Code based on SysML and Mapping Configuration
## General information
This repository contains code for the Proof-of-concept (PoC) implementation done for the Master of Science in Information 
and Software Engineering at the [University of Applied Sciences Dornbirn](https://www.fhv.at/en/).

This work was supervised and conceptually elaborated at the [Chair of Information Systems and Business Process Management (i17), Department of Computer Science, Technical University of Munich](https://www.cs.cit.tum.de/bpm/chair/)

## Description
This PoC implementation is based on a recently published approach to machine learning modeling with SysML at INDIN 2022 (Radler, S., Rigger, E., Mangler, J., Rinderle-Ma, S., 2022. Integration of Machine Learning Task Definition in Model-Based Systems Engineering using SysML, in: 2022 IEEE 20th International Conference on Industrial Informatics (INDIN). Presented at the 2022 IEEE 20th International Conference on Industrial Informatics (INDIN), IEEE, Perth, Australia, pp. 546–551. https://doi.org/10.1109/INDIN51773.2022.9976107).

The goals of the PoC implementation are as follows:
 - Use of an existing SysML profile for machine learning (ML) modeling, which can be used to describe data, interfaces, ML processing, and to define ML algorithms
 - Develop a model transformation to extract information from the SysML model into the intermediate model. The intermediate model must be defined as a metamodel beforehand
 - Generating executable ML code from a SysML model using the intermediate model and template-based code generation
 - Definition of ML templates for specific stereotypes from the user-defined SysML profile, which are used as the basis for generation
 - Provide a mapping configuration mechanism that allows the user to map SysML elements to template variables and exchange them dynamically
 
 
 ## Cite this work
 
MODELS 2023 TODO right after publication is done.

[Contact Us](mailto:simon.raedler@tum.de)
