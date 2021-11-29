// ======================================================================
// \title  BasicTopologyAc.cpp
// \author Generated by fpp-to-cpp
// \brief  cpp file for Basic topology
//
// \copyright
// Copyright (c) 2021 California Institute of Technology.
// U.S. Government sponsorship acknowledged.
// All rights reserved.
// ======================================================================

#include "BasicTopologyAc.hpp"

namespace M {

  namespace {

    // ----------------------------------------------------------------------
    // Component configuration objects
    // ----------------------------------------------------------------------

    namespace ConfigObjects {

      namespace active2 {
        U32 x = 0;
      }

    }

    // ----------------------------------------------------------------------
    // Component instances
    // ----------------------------------------------------------------------

    // active1
    Active active1(FW_OPTIONAL_NAME("active1"));

    // active2
    Active active2;

    // active3
    Active active3(FW_OPTIONAL_NAME("active3"));

    // passive1
    Passive passive1(FW_OPTIONAL_NAME("passive1"));

    // passive2
    Passive passive2(FW_OPTIONAL_NAME("passive2"));

    // ----------------------------------------------------------------------
    // Private functions
    // ----------------------------------------------------------------------

    // Initialize components
    void initComponents(const TopologyState& state) {
      active1.init(QueueSizes::active1, InstanceIds::active1);
      active2.initSpecial();
      active3.init(QueueSizes::active3, InstanceIds::active3);
      passive1.init(InstanceIds::passive1);
      passive2.init(InstanceIds::passive2);
    }

    // Configure components
    void configComponents(const TopologyState& state) {
      active2.config();
    }

    // Set component base Ids
    void setBaseIds() {
      active1.setIdBase(BaseIds::active1);
      active2.setIdBase(BaseIds::active2);
      active3.setIdBase(BaseIds::active3);
      passive1.setIdBase(BaseIds::passive1);
      passive2.setIdBase(BaseIds::passive2);
    }

    // Connect components
    void connectComponents() {

      // C1
      passive1.set_p_OutputPort(
          0,
          active1.get_p_InputPort(0)
      );

      // C2
      passive2.set_p_OutputPort(
          0,
          active2.get_p_InputPort(0)
      );

    }

    // Start tasks
    void startTasks(const TopologyState& state) {
      active1.start(
        static_cast<NATIVE_UINT_TYPE>(Priorities::active1),
        static_cast<NATIVE_UINT_TYPE>(StackSizes::active1),
        static_cast<NATIVE_UINT_TYPE>(CPUs::active1),
        static_cast<NATIVE_UINT_TYPE>(TaskIds::active1)
      );
      active2.startSpecial();
      active3.start(
        static_cast<NATIVE_UINT_TYPE>(Priorities::active3),
        static_cast<NATIVE_UINT_TYPE>(StackSizes::active3),
        Os::Task::TASK_DEFAULT,
        static_cast<NATIVE_UINT_TYPE>(TaskIds::active3)
      );
    }

    // Stop tasks
    void stopTasks(const TopologyState& state) {
      active1.exit();
      active2.stopSpecial();
      active3.exit();
    }

    // Free threads
    void freeThreads(const TopologyState& state) {
      (void) active1.ActiveComponentBase::join(nullptr);
      active2.freeSpecial();
      (void) active3.ActiveComponentBase::join(nullptr);
    }

    // Tear down components
    void tearDownComponents(const TopologyState& state) {
      active2.tearDown();
    }

  }

  // ----------------------------------------------------------------------
  // Public interface functions
  // ----------------------------------------------------------------------

  void setup(const TopologyState& state) {
    initComponents(state);
    configComponents(state);
    setBaseIds();
    connectComponents();
    startTasks(state);
  }

  void teardown(const TopologyState& state) {
    stopTasks(state);
    freeThreads(state);
    tearDownComponents(state);
  }

}
