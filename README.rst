======
README
======

About
-----

This repository contains an Android source code for **Aakash
Programming Lab(APL)**. It contain three major branches, ``ics``,
``froyo`` and ``absolete``. ``ics`` and ``froyo`` will be actively
maintained. As the name suggest ``absolete`` branch is no longer used.


How to use
----------

clone the repository by typing

::

   git clone https://github.com/androportal/apk.git


you can checkout particular branch using

::
   
   git checkout -b <BRANCH_NAME> /remotes/origin/<BRANCH_NAME>


for example, if you want to ``checkout`` branch `froyo`, then type

::

   git checkout -b froyo /remotes/origin/froyo
   

you can switch branch using

::

   git checkout <BRANCH_NAME>

for example, if you want to visit branch `froyo`, then type

::

   git checkout froyo


Working with project using `eclipse <http://www.eclipse.org/>`_
---------------------------------------------------------------

- After you clone the repository, switch to specific branch of your
  choice.
- Open ``eclipse`` and goto ``file`` -> ``import`` -> ``Android`` ->
  ``Existing Android Code into Workspace``, click `Next` and browse
  the repository. click `OK` and `Finish`

